package at.ac.tuwien.sepm.groupphase.backend.events.organisation;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class OrganisationEditEvent extends OrganisationEvent {
    public OrganisationEditEvent(String name) {
        super(name);
    }
}
