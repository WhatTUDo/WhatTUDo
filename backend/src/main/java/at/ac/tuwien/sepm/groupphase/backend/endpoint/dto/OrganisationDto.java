package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class OrganisationDto {
    private Integer id;
    private String name;
    private List<Integer> calendarIds = Collections.emptyList();
}
