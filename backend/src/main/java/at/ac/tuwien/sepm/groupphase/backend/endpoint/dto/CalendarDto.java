package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.CalendarEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarDto extends PermissionDto {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private List<Integer> organizationIds;
    @NonNull
    private List<Integer> eventIds;
    @NonNull
    private String description;
    private boolean canCreateEvents;

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return CalendarEndpoint.BASE_URL + "/" + this.id + "/cover";
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
