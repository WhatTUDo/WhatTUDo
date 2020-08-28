package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LabelDto extends BaseDto {
    private Integer id;
    private String name;
    private List<Integer> eventIds;
}
