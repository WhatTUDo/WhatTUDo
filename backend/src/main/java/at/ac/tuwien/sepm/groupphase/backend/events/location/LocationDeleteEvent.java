package at.ac.tuwien.sepm.groupphase.backend.events.location;

import at.ac.tuwien.sepm.groupphase.backend.events.label.LabelEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LocationDeleteEvent extends LocationEvent {
    public LocationDeleteEvent(String name) {
        super(name);
    }
}
