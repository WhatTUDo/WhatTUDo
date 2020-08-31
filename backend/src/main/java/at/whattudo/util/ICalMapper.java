package at.whattudo.util;

import at.whattudo.entity.Calendar;
import at.whattudo.entity.Event;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.TimezoneAssignment;
import biweekly.io.TimezoneInfo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class ICalMapper {
    @Transactional
    public ICalendar mapCalendar(String name, List<VEvent> events) {
        ICalendar iCalendar = new ICalendar();
        TimezoneInfo tzinfo = new TimezoneInfo();
        tzinfo.setDefaultTimezone(TimezoneAssignment.download(TimeZone.getDefault(), true));
        iCalendar.setTimezoneInfo(tzinfo);
        iCalendar.setProductId("WhatTUDo");
        iCalendar.setName(name);
        events.forEach(iCalendar::addEvent);
        return iCalendar;
    }

    @Transactional
    public VEvent mapEvent(Event event) {
        VEvent vEvent = new VEvent();
        vEvent.setUid(event.getId().toString());
        vEvent.setSummary(event.getName());
        if (event.getLocation() != null) vEvent.setLocation(event.getLocation().getName());
        if (event.getDescription() != null) vEvent.setDescription(event.getDescription());
        vEvent.setDateStart(Date.from(event.getStartDateTime().atZone(ZoneId.of("UTC")).toInstant()));
        vEvent.setDateEnd(Date.from(event.getEndDateTime().atZone(ZoneId.of("UTC")).toInstant()));
        return vEvent;
    }

    @Transactional
    public List<VEvent> flatMapEvents(List<Calendar> calendars) {
        return calendars.stream()
            .flatMap(calendar -> calendar.getEvents().stream().map(this::mapEvent))
            .collect(Collectors.toList());
    }
}
