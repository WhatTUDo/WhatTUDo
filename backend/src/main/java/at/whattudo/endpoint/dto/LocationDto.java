package at.whattudo.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocationDto extends BaseDto {
    private Integer id;
    private String name;
    private String address;
    private String zip;
    private double latitude;
    private double longitude;
    private List<Integer> eventIds;
}
