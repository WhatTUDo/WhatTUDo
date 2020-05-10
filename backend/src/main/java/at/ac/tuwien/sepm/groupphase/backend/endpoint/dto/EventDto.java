package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

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
}
