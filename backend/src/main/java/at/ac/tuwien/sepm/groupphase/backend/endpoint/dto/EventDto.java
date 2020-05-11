package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;


@Value
public class EventDto {
    Integer id;
    String name;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    Integer calendarId;
    
}
