package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCollisionDto {
    private EventDto event;
    private int collisionScore;
    private String message;

}
