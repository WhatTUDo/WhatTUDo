package at.whattudo.events.location;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationUpdateEvent extends LocationEvent {
    public LocationUpdateEvent(String name) {
        super(name);
    }
}
