package at.whattudo.endpoint.mapper;

import at.whattudo.endpoint.dto.EventCollisionDto;
import at.whattudo.entity.EventCollision;
import org.mapstruct.Mapper;

import javax.transaction.Transactional;

@Transactional
@Mapper(componentModel = "spring")
public abstract class EventCollisionMapper {

    public abstract EventCollisionDto eventCollisionToEventCollisionDto(EventCollision eventCollision);
}
