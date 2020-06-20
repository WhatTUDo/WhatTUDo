package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.ICalService;
import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleICalService implements ICalService {
    private final CalendarService calendarService;
    private final SubscriptionService subscriptionService;

    @Override
    @Transactional
    public ICalendar getAllCalendars() throws ServiceException {
        List<Calendar> calendars = calendarService.findAll();
        return toCalendar("WhatTUDo", flatMapEvents(calendars));
    }

    @Override
    @Transactional
    public ICalendar getCalendar(Integer id) throws ServiceException {
        Calendar calendar = calendarService.findById(id);
        List<VEvent> events = calendar.getEvents().stream()
            .map(this::mapEvent)
            .collect(Collectors.toList());
        return toCalendar(calendar.getName(), events);
    }

    @Override
    @Transactional
    public ICalendar getCalendarsForUser(ApplicationUser user) throws ServiceException {
        List<Calendar> calendars = subscriptionService.getSubsribedCalendarsForUser(user);
        return toCalendar("WhatTUDo " + user.getName(), flatMapEvents(calendars));
    }

    @Transactional
    ICalendar toCalendar(String name, List<VEvent> events) {
        ICalendar iCalendar = new ICalendar();
        iCalendar.setProductId("WhatTUDo");
        iCalendar.setName(name);
        events.forEach(iCalendar::addEvent);
        return iCalendar;
    }

    @Transactional
    VEvent mapEvent(Event event) {
        VEvent vEvent = new VEvent();
        vEvent.setUid(event.getId().toString());
        vEvent.setSummary(event.getName());
        vEvent.setDateStart(Date.from(event.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        vEvent.setDateEnd(Date.from(event.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        return vEvent;
    }

    @Transactional
    List<VEvent> flatMapEvents(List<Calendar> calendars) {
        return calendars.stream()
            .flatMap(calendar -> calendar.getEvents().stream().map(this::mapEvent))
            .collect(Collectors.toList());
    }
}
