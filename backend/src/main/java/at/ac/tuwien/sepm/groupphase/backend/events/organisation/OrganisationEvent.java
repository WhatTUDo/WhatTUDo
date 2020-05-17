package at.ac.tuwien.sepm.groupphase.backend.events.organisation;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class OrganisationEvent extends CustomEvent {
    public OrganisationEvent(String elementName) {
        super(elementName);
    }
}
