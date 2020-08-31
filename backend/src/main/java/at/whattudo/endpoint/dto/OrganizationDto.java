package at.whattudo.endpoint.dto;

import at.whattudo.endpoint.OrganizationEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationDto extends PermissionDto {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private List<Integer> calendarIds;

    private boolean canCreateCalendar;
    private String description;

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return OrganizationEndpoint.BASE_URL + "/" + this.id + "/cover";
    }

    public OrganizationDto() {
    }

    public OrganizationDto(Integer id, String name, List<Integer> calendarIds, String description) {
        this.id = id;
        this.name = name;
        this.calendarIds = calendarIds;
        this.description = description;
    }


}
