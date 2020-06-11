package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDto extends PermissionDto {
    private Integer id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer calendarId;
}
