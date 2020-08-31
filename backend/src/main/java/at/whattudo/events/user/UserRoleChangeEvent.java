package at.whattudo.events.user;

import at.whattudo.entity.Organization;
import at.whattudo.entity.OrganizationRole;
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
