package at.whattudo.events.location;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationCreateEvent extends LocationEvent {
    public LocationCreateEvent(String name) {
        super(name);
    }
}
