package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.ICalService;
import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import at.ac.tuwien.sepm.groupphase.backend.util.ICalMapper;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.io.TimezoneInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleICalService implements ICalService {
    private final CalendarService calendarService;
    private final SubscriptionService subscriptionService;
    private final ICalMapper iCalMapper;

    @Override
    @Transactional
    public ICalendar getAllCalendars() throws ServiceException {
        List<Calendar> calendars = calendarService.findAll();
        return iCalMapper.mapCalendar("WhatTUDo", iCalMapper.flatMapEvents(calendars));
    }

    @Override
    @Transactional
    public ICalendar getCalendar(Integer id) throws ServiceException {
        Calendar calendar = calendarService.findById(id);
        List<VEvent> events = calendar.getEvents().stream()
            .map(iCalMapper::mapEvent)
            .collect(Collectors.toList());
        return iCalMapper.mapCalendar(calendar.getName(), events);
    }

    @Override
    @Transactional
    public ICalendar getCalendarsForUser(ApplicationUser user) throws ServiceException {
        List<Calendar> calendars = subscriptionService.getSubsribedCalendarsForUser(user);
        return iCalMapper.mapCalendar("WhatTUDo " + user.getName(), iCalMapper.flatMapEvents(calendars));
    }
}
