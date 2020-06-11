package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventCollisionDto extends BaseDto {
    private EventDto event;
    private int collisionScore;
    private String message;

}
