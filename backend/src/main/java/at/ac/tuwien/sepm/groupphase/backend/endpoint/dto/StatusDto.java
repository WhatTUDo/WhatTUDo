package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatusPossibilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private Integer userId;
    private Integer eventId;
    private AttendanceStatusPossibilities attendanceStatusPossibilitiesDto;
    private LocalDateTime lastModifiedDto;
}
