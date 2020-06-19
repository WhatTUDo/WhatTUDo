package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.OrganizationEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
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

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return OrganizationEndpoint.BASE_URL + "/" + this.id + "/cover";
    }
}
