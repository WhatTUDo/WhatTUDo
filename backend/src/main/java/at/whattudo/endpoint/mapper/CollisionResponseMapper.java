package at.whattudo.endpoint.mapper;

import at.whattudo.endpoint.dto.CollisionResponseDto;
import at.whattudo.endpoint.dto.EventCollisionDto;
import at.whattudo.entity.EventCollision;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Mapper(componentModel = "spring")
public class CollisionResponseMapper {

    @Autowired
    EventCollisionMapper eventCollisionMapper;

    public CollisionResponseDto mapCollisionResponseDto(List<EventCollision> eventCollisions, List<LocalDateTime[]> dateRecommendations) {
        List<EventCollisionDto> eventCollisionDtos = new ArrayList<>();
        for (EventCollision eventCollision : eventCollisions) {
            eventCollisionDtos.add(eventCollisionMapper.eventCollisionToEventCollisionDto(eventCollision));
        }
        return new CollisionResponseDto(eventCollisionDtos, dateRecommendations);
    }
}
