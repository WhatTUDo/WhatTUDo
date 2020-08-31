package at.whattudo.events.organization;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class OrganizationEvent extends CustomEvent {
    public OrganizationEvent(String elementName) {
        super(elementName);
    }
}
