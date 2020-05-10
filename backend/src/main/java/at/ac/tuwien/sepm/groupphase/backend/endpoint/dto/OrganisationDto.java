package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Value;

import java.util.List;

@Value
public class OrganisationDto {
    Integer id;
    String name;
    List<Integer> calendarIds;
}
