package at.whattudo.events.organization;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class OrganizationCreateEvent extends OrganizationEvent {
    public OrganizationCreateEvent(String elementName) {
        super(elementName);
    }
}
