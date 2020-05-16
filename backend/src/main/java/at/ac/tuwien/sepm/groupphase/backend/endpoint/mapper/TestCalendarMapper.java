package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
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
    @Autowired protected OrganisationRepository organisationRepository;

    @Autowired protected SimpleEventService eventService;

    public abstract CalendarDto calendarToCalendarDto(Calendar calendar);

    @BeforeMapping
    protected void mapOrganisations(Calendar calendar, @MappingTarget CalendarDto calendarDto) {

        List<Integer> orgaIds = new ArrayList<Integer>();
        for(Organisation o : calendar.getOrganisations()){
            orgaIds.add(o.getId());
        }

        calendarDto.setOrganisationIds(orgaIds);

        List<Integer> EventIds = new ArrayList<Integer>();
        for(Event e : calendar.getEvents())
            EventIds.add(e.getId());
        calendarDto.setEventIds(EventIds);
    }

    public abstract Calendar calendarDtoToCalendar(CalendarDto calendarDto);

    @BeforeMapping
    protected void mapCalendar(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        List<Organisation> orgas = new ArrayList<Organisation>();

        for (Integer o : calendarDto.getOrganisationIds()) {
            orgas.add(organisationRepository.findById(o).orElseThrow(() -> new NotFoundException("No Organisation with this ID")));

            calendar.setOrganisations(orgas);
        }

        List<Event> eventList = new ArrayList<Event>();
        for(Integer e : calendarDto.getEventIds())
            //if following line is not allowed, then use: eventList.add(eventRepository.findById(e).orElseThrow(() -> new NotFoundException("No Event with this ID"));
            eventList.add(eventService.findById(e));
        calendar.setEvents(eventList);

    }

    public List<CalendarDto> calendarsToCalendarDtos(List <Calendar> calendars){

        List<CalendarDto> calendarfinal = new ArrayList<CalendarDto>();
        for(Calendar c : calendars){
            calendarfinal.add(calendarToCalendarDto(c));
        }


        return calendarfinal;
    }
   /* CalendarService calendarService;

    @Autowired
    public EventMapper(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Event dtoToEntity(EventDto eventDto) {
        Calendar calendar = calendarService.findById(eventDto.getCalendarId());
        return new Event(eventDto.getId(), eventDto.getName(), eventDto.getStartDateTime(), eventDto.getEndDateTime(), calendar);
    }

    public EventDto entityToDto(Event event) {
        return new EventDto(event.getId(), event.getName(), event.getStartDateTime(), event.getEndDateTime(), (event.getCalendar()).getId());
    }*/
}
