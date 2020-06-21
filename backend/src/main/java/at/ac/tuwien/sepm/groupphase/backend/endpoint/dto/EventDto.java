package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDto extends PermissionDto {
    private Integer id;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer calendarId;
    private Integer locationId;
    private String description;

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return EventEndpoint.BASE_URL + "/" + this.id + "/cover";
    }

    public EventDto() {
    }

    public EventDto(Integer id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer calendarId, Integer locationId) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.calendarId = calendarId;
        this.locationId = locationId;
    }

}
