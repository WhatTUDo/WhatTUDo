package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarDto extends PermissionDto {
    private Integer id;
    private String name;
    private List<Integer> organizationIds;
    private List<Integer> eventIds;
}
