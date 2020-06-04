package at.ac.tuwien.sepm.groupphase.backend.events.user;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserRoleChangeEvent extends UserEvent {
    private final Organization organization;
    private final OrganizationRole role;

    public UserRoleChangeEvent(String elementName, Organization organization, OrganizationRole role) {
        super(elementName);
        this.organization = organization;
        this.role = role;
    }
}
