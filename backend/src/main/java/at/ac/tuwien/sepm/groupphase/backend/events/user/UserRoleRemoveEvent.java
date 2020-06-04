package at.ac.tuwien.sepm.groupphase.backend.events.user;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserRoleRemoveEvent extends UserEvent {
    private final Organization organization;

    public UserRoleRemoveEvent(String elementName, Organization organization) {
        super(elementName);
        this.organization = organization;
    }
}
