package at.whattudo.service.impl;

import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.service.CalendarService;
import at.whattudo.service.ICalService;
import at.whattudo.service.SubscriptionService;
import at.whattudo.util.ICalMapper;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
