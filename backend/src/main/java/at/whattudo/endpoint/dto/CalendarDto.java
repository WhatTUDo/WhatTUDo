package at.whattudo.endpoint.dto;

import at.whattudo.endpoint.CalendarEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CalendarDto extends PermissionDto {
    private Integer id;
    private String name;
    private List<Integer> organizationIds;
    private List<Integer> eventIds;
    private String description;
    private boolean canCreateEvents;

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return CalendarEndpoint.BASE_URL + "/" + this.id + "/cover";
    }

    public CalendarDto(Integer id, String name, List<Integer> organizationIds, List<Integer> eventIds, String description) {
        this.id = id;
        this.name = name;
        this.organizationIds = organizationIds;
        this.eventIds = eventIds;
        this.description = description;
    }

    public CalendarDto(Integer id, String name, List<Integer> organizationIds, List<Integer> eventIds) {
        this.id = id;
        this.name = name;
        this.organizationIds = organizationIds;
        this.eventIds = eventIds;
    }

    public CalendarDto() {
    }
}
