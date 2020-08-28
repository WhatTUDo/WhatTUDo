package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This should not be stored in the database. It should be created dynamically at runtime.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCollision {

    @NonNull
    private Event event;

    @NonNull
    private int collisionScore;

    @NonNull
    private String message;
}
