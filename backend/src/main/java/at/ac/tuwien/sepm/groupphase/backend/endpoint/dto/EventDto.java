package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;
import org.hibernate.annotations.SelectBeforeUpdate;
import java.time.LocalDateTime;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class EventDto {
    private Integer id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer calendarId;


    public EventDto(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer calendarId) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.calendarId = calendarId;
    }
}
