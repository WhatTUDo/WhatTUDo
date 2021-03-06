package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarCreateDto;
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
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class CalendarMapper {
    @Autowired
    protected OrganizationRepository organizationRepository;
    @Autowired
    protected CalendarRepository calendarRepository;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected PermissionEvaluator permissionEvaluator;

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

    @BeforeMapping
    protected void mapPermissions(Calendar calendar, @MappingTarget CalendarDto calendarDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        calendarDto.setCanEdit(permissionEvaluator.hasPermission(authentication, calendar, "MOD"));
        calendarDto.setCanDelete(permissionEvaluator.hasPermission(authentication, calendar, "MOD"));
        calendarDto.setCanCreateEvents(permissionEvaluator.hasPermission(authentication, calendar, "MEMBER"));
    }

    public abstract Calendar calendarCreateDtoToCalendar(CalendarCreateDto calendarCreateDto);

    @BeforeMapping
    protected void mapOrganizations(CalendarCreateDto calendarCreateDto, @MappingTarget Calendar calendar) {
        List<Organization> orgas = new ArrayList<>();
        orgas.add(organizationRepository.findById(calendarCreateDto.getOrgaId()).orElseThrow(() -> new NotFoundException("No Organization with this ID")));
        calendar.setOrganizations(orgas);
    }

    @BeforeMapping
    protected void mapEvents(@SuppressWarnings("unused") CalendarCreateDto calendarDto, @MappingTarget Calendar calendar) {
        calendar.setEvents(new ArrayList<>());
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

    public abstract List<CalendarDto> calendarsToCalendarDtos(List<Calendar> calendars);
}
