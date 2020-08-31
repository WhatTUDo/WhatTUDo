package at.whattudo.events.location;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationDeleteEvent extends LocationEvent {
    public LocationDeleteEvent(String name) {
        super(name);
    }
}
