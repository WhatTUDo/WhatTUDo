package at.ac.tuwien.sepm.groupphase.backend.events.organization;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class OrganizationEvent extends CustomEvent {
    public OrganizationEvent(String elementName) {
        super(elementName);
    }
}
