package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleEventService;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TestCalendarMapper {
    @Autowired
    protected OrganizationRepository organizationRepository;

    @Autowired
    protected SimpleEventService eventService;

    public abstract CalendarDto calendarToCalendarDto(Calendar calendar);

    @BeforeMapping
    protected void mapOrganizations(Calendar calendar, @MappingTarget CalendarDto calendarDto) {

        List<Integer> orgaIds = new ArrayList<Integer>();
        for (Organization o : calendar.getOrganizations()) {
            orgaIds.add(o.getId());
        }

        calendarDto.setOrganizationIds(orgaIds);

        List<Integer> EventIds = new ArrayList<Integer>();
        for (Event e : calendar.getEvents())
            EventIds.add(e.getId());
        calendarDto.setEventIds(EventIds);
    }

    public abstract Calendar calendarDtoToCalendar(CalendarDto calendarDto);

    @BeforeMapping
    protected void mapCalendar(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        List<Organization> orgas = new ArrayList<Organization>();

        for (Integer o : calendarDto.getOrganizationIds()) {
            orgas.add(organizationRepository.findById(o).orElseThrow(() -> new NotFoundException("No Organization with this ID")));

            calendar.setOrganizations(orgas);
        }

        List<Event> eventList = new ArrayList<Event>();
        for (Integer e : calendarDto.getEventIds())
            //if following line is not allowed, then use: eventList.add(eventRepository.findById(e).orElseThrow(() -> new NotFoundException("No Event with this ID"));
            eventList.add(eventService.findById(e));
        calendar.setEvents(eventList);

    }

    public List<CalendarDto> calendarsToCalendarDtos(List<Calendar> calendars) {

        List<CalendarDto> calendarFinal = new ArrayList<CalendarDto>();
        for (Calendar c : calendars) {
            calendarFinal.add(calendarToCalendarDto(c));
        }


        return calendarFinal;
    }
}
