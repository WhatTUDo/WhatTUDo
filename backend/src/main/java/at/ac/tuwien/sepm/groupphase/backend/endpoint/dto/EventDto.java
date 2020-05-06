package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import java.time.LocalDateTime;

public class EventDto {
    private Integer id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private CalendarDto calendardto;
}
