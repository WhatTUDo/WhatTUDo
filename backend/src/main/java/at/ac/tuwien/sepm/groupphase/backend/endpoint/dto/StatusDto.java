package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatusPossibilities;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StatusDto extends BaseDto {
    private Integer id;
    @NonNull
    private String username;
    @NonNull
    private Integer eventId;
    @NonNull
    private AttendanceStatusPossibilities status;
}
