package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;

import java.util.Collections;
import java.util.List;

public class CalendarDto {
    private Integer id;
    private String name;
    private List<OrganisationDto> organisationsDto;
    private List<EventDto> eventsDto = Collections.emptyList();
}
