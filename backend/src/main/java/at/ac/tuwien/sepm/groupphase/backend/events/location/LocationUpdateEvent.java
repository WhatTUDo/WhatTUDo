package at.ac.tuwien.sepm.groupphase.backend.events.location;

import at.ac.tuwien.sepm.groupphase.backend.events.label.LabelEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationUpdateEvent extends LocationEvent {
    public LocationUpdateEvent(String name) {
        super(name);
    }
}
