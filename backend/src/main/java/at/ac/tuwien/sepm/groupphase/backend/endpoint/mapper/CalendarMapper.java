package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses= EventMapper.class)
public interface CalendarMapper {

    @Mapping(target = "events", source = "events",qualifiedByName = "eventDtoList")
    CalendarDto entityToDto(Calendar calendar);

    @Mapping(target = "events", source = "events",qualifiedByName = "eventList")
    Calendar dtoToEntity(CalendarDto calendarDto);


}
