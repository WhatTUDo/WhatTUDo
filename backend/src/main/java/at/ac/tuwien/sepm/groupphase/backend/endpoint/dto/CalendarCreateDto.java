package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.List;

@Data
//@NoArgsConstructor
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
