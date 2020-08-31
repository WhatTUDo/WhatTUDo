package at.whattudo.endpoint.mapper;


import at.whattudo.endpoint.dto.LabelDto;
import at.whattudo.entity.Label;
import at.whattudo.entity.Event;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.LabelRepository;
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
