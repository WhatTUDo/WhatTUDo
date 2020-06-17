package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

import java.util.List;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarDto extends PermissionDto {
    private Integer id;
    private String name;
    private List<Integer> organizationIds;
    private List<Integer> eventIds;
    private String description;

    public CalendarDto(Integer id, String name, List<Integer> organizationIds, List<Integer> eventIds) {
        this.id = id;
        this.name = name;
        this.organizationIds = organizationIds;
        this.eventIds = eventIds;
    }

    public CalendarDto() {
    }
}
