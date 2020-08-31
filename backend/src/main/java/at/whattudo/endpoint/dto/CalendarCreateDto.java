package at.whattudo.endpoint.dto;

import lombok.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarCreateDto extends BaseDto {
    private String name;
    private Integer orgaId;
    private String description;

    public CalendarCreateDto(String name, Integer orgaId) {
        this.name = name;
        this.orgaId = orgaId;
    }

    public CalendarCreateDto() {
    }



}
