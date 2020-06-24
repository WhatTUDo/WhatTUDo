package at.ac.tuwien.sepm.groupphase.backend.events.location;

import at.ac.tuwien.sepm.groupphase.backend.events.label.LabelEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationCreateEvent extends LocationEvent {
    public LocationCreateEvent(String name) {
        super(name);
    }
}
