package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollisionResponseDto {

    private List<EventCollisionDto> eventCollisions;
    private List<LocalDateTime[]> dateSuggestions;
}
