package at.whattudo.events.user;

import at.whattudo.entity.Organization;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserRoleRemoveEvent extends UserEvent {
    private final Organization organization;

    public UserRoleRemoveEvent(String elementName, Organization organization) {
        super(elementName);
        this.organization = organization;
    }
}
