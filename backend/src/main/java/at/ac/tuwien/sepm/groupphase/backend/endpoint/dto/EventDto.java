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
    private CalendarDto calendar;

    public EventDto(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, CalendarDto calendar) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.calendar = calendar;
    }
}
