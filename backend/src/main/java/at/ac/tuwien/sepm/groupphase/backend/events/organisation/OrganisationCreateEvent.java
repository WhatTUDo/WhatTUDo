package at.ac.tuwien.sepm.groupphase.backend.events.organisation;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class OrganisationCreateEvent extends OrganisationEvent {
    public OrganisationCreateEvent(String elementName) {
        super(elementName);
    }
}
