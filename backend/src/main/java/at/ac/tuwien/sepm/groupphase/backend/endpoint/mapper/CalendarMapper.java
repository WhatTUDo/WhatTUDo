package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class CalendarMapper {
    @Autowired protected OrganizationRepository organizationRepository;
    @Autowired protected CalendarRepository calendarRepository;
    @Autowired protected EventRepository eventRepository;

    public abstract CalendarDto calendarToCalendarDto(Calendar calendar);

    @BeforeMapping
    protected void mapOrganizations(Calendar calendar, @MappingTarget CalendarDto calendarDto) {
        calendarDto.setOrganizationIds(calendar.getOrganizations().stream().map(Organization::getId).collect(Collectors.toList()));
    }

    @BeforeMapping
    protected void mapEvents(Calendar calendar, @MappingTarget CalendarDto calendarDto) {
        Calendar calEntity = calendarRepository.findById(calendar.getId())
            .orElseThrow(() -> new NotFoundException("This calendar does not exist in the database"));

        calendarDto.setEventIds(calEntity.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
    }

    public abstract Calendar calendarDtoToCalendar(CalendarDto calendarDto);

    @BeforeMapping
    protected void mapOrganizations(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        calendar.setOrganizations(organizationRepository.findAllById(calendarDto.getOrganizationIds()));
    }

    @BeforeMapping
    protected void mapEvents(CalendarDto calendarDto, @MappingTarget Calendar calendar) {
        calendar.setEvents(eventRepository.findAllById(calendarDto.getEventIds()));
    }
}
