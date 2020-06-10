package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CollisionResponseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventCollisionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Mapper(componentModel = "spring")
public class CollisionResponseMapper {

    @Autowired EventCollisionMapper eventCollisionMapper;

    public CollisionResponseDto mapCollisionResponseDto(List<EventCollision> eventCollisions, List<LocalDateTime> dateRecommendations) {
        List<EventCollisionDto> eventCollisionDtos = new ArrayList<>();
        for(EventCollision eventCollision : eventCollisions) {
            eventCollisionDtos.add(eventCollisionMapper.eventCollisionToEventCollisionDto(eventCollision));
        }
        return new CollisionResponseDto(eventCollisionDtos, dateRecommendations);
    }
}
