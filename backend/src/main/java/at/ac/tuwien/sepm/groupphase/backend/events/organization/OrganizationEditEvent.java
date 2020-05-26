package at.ac.tuwien.sepm.groupphase.backend.events.organization;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class OrganizationEditEvent extends OrganizationEvent {
    public OrganizationEditEvent(String name) {
        super(name);
    }
}
