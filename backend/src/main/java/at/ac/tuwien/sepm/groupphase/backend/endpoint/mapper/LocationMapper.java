package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class LocationMapper {
    @Autowired
    protected LocationRepository locationRepository;
    @Autowired
    protected EventRepository eventRepository;

    @BeforeMapping
    protected void mapEvents(Location location, @MappingTarget LocationDto locationDto) {
        Location locEntity = locationRepository.findById(location.getId())
            .orElseThrow(() -> new NotFoundException("This location does not exist in the database"));

        locationDto.setEventIds(locEntity.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
    }

    public abstract LocationDto locationToLocationDto(Location location);

    @BeforeMapping
    public void mapEvents(LocationDto locationDto, @MappingTarget Location location) {
        if (locationDto.getEventIds() != null && locationDto.getEventIds().size() > 0) {
            location.setEvents(eventRepository.findAllById(locationDto.getEventIds()));
        } else {
            location.setEvents(new ArrayList<>());
        }
    }

    public abstract Location locationDtoToLocation(LocationDto locationDto);


}
