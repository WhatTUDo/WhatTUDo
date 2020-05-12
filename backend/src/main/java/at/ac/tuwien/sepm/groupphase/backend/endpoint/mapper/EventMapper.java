package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    CalendarService calendarService;

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
    }
}
