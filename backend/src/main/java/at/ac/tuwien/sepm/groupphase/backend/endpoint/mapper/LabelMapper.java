package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class LabelMapper {
    @Autowired
    protected LabelRepository labelRepository;
    @Autowired
    protected EventRepository eventRepository;

    public abstract LabelDto labelToLabelDto(Label label);

    @BeforeMapping
    protected void mapEvents(Label label, @MappingTarget LabelDto labelDto) {
        Label labEntity = labelRepository.findById(label.getId())
            .orElseThrow(() -> new NotFoundException("This label does not exist in the database"));

        labelDto.setEventIds(labEntity.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
    }

    public abstract Label labelDtoToLabel(LabelDto labelDto);

    @BeforeMapping
    public void mapEvents(LabelDto labelDto, @MappingTarget Label label) {
        label.setEvents(eventRepository.findAllById(labelDto.getEventIds()));
    }

}
