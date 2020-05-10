package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Value;

import java.util.List;

@Value
public class CalendarDto {
    Integer id;
    String name;
    List<Integer> organisationIds;
    List<Integer> eventIds;
}
