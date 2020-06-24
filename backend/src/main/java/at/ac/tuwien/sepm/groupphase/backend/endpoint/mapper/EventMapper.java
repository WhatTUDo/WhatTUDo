package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Autowired
    protected CalendarRepository calendarRepository;
    @Autowired
    protected LocationRepository locationRepository;
    @Autowired
    protected PermissionEvaluator permissionEvaluator;

    public abstract EventDto eventToEventDto(Event event);

    @BeforeMapping
    protected void mapCalendar(Event event, @MappingTarget EventDto eventDto) {
        eventDto.setCalendarId(event.getCalendar().getId());
    }

    @BeforeMapping
    protected void mapLocation(Event event, @MappingTarget EventDto eventDto){
        if (event.getLocation() != null) {
            eventDto.setLocationId(event.getLocation().getId());
        }
    }

    @BeforeMapping
    protected void mapPermissions(Event event, @MappingTarget EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        eventDto.setCanEdit(permissionEvaluator.hasPermission(authentication, event, "MEMBER"));
        eventDto.setCanDelete(permissionEvaluator.hasPermission(authentication, event, "MEMBER"));
    }

    public abstract Event eventDtoToEvent(EventDto eventDto);

    @BeforeMapping
    protected void mapCalendar(EventDto eventDto, @MappingTarget Event event) {
        Calendar cal = calendarRepository.findById(eventDto.getCalendarId()).orElseThrow(() -> new NotFoundException("No Calendar with this ID"));
        event.setCalendar(cal);
    }

    @BeforeMapping
    protected void mapLocation(EventDto eventDto, @MappingTarget Event event){
        if(eventDto.getLocationId() != null) {
            Location location = locationRepository.findById(eventDto.getLocationId()).orElseThrow(() -> new NotFoundException("No Location with this ID"));
            event.setLocation(location);
        }
    }

    public abstract List<EventDto> eventListToeventDtoList(List<Event> eventList);

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
