package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    @Autowired protected CalendarRepository calendarRepository;

    public abstract EventDto eventToEventDto(Event event);

    @BeforeMapping
    protected void mapCalendar(Event event, @MappingTarget EventDto eventDto) {
        eventDto.setCalendarId(event.getCalendar().getId());
    }

    public abstract Event eventDtoToEvent(EventDto eventDto);

    @BeforeMapping
    protected void mapCalendar(EventDto eventDto, @MappingTarget Event event) {
        Calendar cal = calendarRepository.findById(eventDto.getCalendarId()).orElseThrow(() -> new NotFoundException("No Calendar with this ID"));
        event.setCalendar(cal);
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
