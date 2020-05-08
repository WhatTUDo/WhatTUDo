package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import lombok.*;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CalendarDto {
    private Integer id;
    private String name;
    private List<Organisation> organisations;
    private List<EventDto> events;

    public CalendarDto(Integer id, String name, List<Organisation> organisations) {
        this.id = id;
        this.name = name;
        this.organisations = organisations;
    }
}
