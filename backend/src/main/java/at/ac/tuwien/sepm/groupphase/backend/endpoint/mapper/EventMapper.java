package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

//TODO: annotations

@Mapper(uses = CalendarMapper.class)
public interface EventMapper {

    @Mapping(target = "calendar", ignore = true)
    EventDto entityToDto(Event event);

    @Mapping(target = "calendar",ignore = true)
    Event dtoToEntity(EventDto eventDto);

    @Named("eventDtoList")
    default List<EventDto>  entityToEventDtoList(List<Event> source){
        return source
            .stream()
            .map(this::entityToDto)
            .peek(dto -> dto.setCalendar(null))
            .collect(Collectors.toList());
    }

    @Named("eventList")
    default List<Event>  dtoToEventEntityList(List<EventDto> source){
        return source
            .stream()
            .map(this::dtoToEntity)
            .peek(dto -> dto.setCalendar(null))
            .collect(Collectors.toList());
    }



//    public EventDto entityToDto(Event event){
//        return new EventDto(event.getName(), event.getStartDateTime(), event.getEndDateTime(), event.getId());
//    }
//
//    public Event dtoToEntity(EventDto event){
//        return new Event(event.getName(),event.getStartDateTime(),event.getEndDateTime());
//    }

}
