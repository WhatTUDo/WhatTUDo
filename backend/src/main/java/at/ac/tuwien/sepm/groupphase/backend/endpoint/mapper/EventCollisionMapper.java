package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCollisionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
@Mapper(componentModel = "spring")
public abstract class EventCollisionMapper {

    public abstract EventCollisionDto eventCollisionToEventCollisionDto(EventCollision eventCollision);
}
