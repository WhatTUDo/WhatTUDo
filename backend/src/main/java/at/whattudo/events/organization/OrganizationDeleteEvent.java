package at.whattudo.events.organization;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class OrganizationDeleteEvent extends OrganizationEvent {
    public OrganizationDeleteEvent(String name) {
        super(name);
    }
}
