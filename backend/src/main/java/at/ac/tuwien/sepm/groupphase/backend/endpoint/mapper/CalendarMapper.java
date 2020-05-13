package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CalendarMapper {
    @Autowired protected OrganisationRepository organisationRepository;
    @Autowired protected EventRepository eventRepository;

    public abstract CalendarDto calendarToCalendarDto(Calendar calendar);

    @BeforeMapping
    protected void mapOrganisations(Calendar calendar, @MappingTarget CalendarDto calendarDto) {
        calendarDto.setOrganisationIds(calendar.getOrganisations().stream().map(Organisation::getId).collect(Collectors.toList()));
    }

    @BeforeMapping
    protected void mapEvents(Calendar calendar, @MappingTarget CalendarDto calendarDto) {
        calendarDto.setEventIds(calendar.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
    }

    public abstract Calendar calendarDtoToCalendar(CalendarDto calendarDto);

    @BeforeMapping
    protected void mapOrganisations(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        calendar.setOrganisations(organisationRepository.findAllById(calendarDto.getOrganisationIds()));
    }

    @BeforeMapping
    protected void mapEvents(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        calendar.setEvents(eventRepository.findAllById(calendarDto.getEventIds()));
    }
}
