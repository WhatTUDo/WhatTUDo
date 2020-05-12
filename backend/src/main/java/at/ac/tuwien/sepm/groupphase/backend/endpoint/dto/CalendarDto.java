package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private Integer id;
    private String name;
    private List<Integer> organisationIds;
    private List<Integer> eventIds;
}
