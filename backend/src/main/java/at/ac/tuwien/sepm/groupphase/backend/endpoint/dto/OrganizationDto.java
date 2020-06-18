package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.OrganizationEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationDto extends PermissionDto {
    private Integer id;
    private String name;
    private List<Integer> calendarIds;

    @JsonProperty("coverImageUrl")
    private String calculateCoverImageUrl() {
        return OrganizationEndpoint.BASE_URL + "/" + this.id + "/cover";
    }
}
