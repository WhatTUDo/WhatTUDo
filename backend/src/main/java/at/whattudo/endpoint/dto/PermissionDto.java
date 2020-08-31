package at.whattudo.endpoint.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PermissionDto extends BaseDto {
    private boolean canEdit;
    private boolean canDelete;
}
