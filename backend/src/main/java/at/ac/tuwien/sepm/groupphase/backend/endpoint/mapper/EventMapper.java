package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.stereotype.Component;

//TODO: annotations

@Component
public class EventMapper {

    public EventDto entityToDto(Event event){
        return new EventDto(event.getName(), event.getStartDateTime(), event.getEndDateTime(), event.getCalendar(), event.getId());
    }

    public Event dtoToEntity(EventDto event){
       return new Event(event.getName(),event.getStartDate(),event.getEndDate(), event.getCalendar());
    }

}
