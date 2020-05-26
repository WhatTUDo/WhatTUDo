package at.ac.tuwien.sepm.groupphase.backend.events;

public class OrganizationCreateEvent extends OrganizationEvent {
    public OrganizationCreateEvent(String elementName) {
        super(elementName);
    }
}
