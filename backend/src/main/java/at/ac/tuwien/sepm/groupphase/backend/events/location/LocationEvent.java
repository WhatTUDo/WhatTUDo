package at.ac.tuwien.sepm.groupphase.backend.events.location;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class LocationEvent extends CustomEvent {
    public LocationEvent(String elementName) {
        super(elementName);
    }
}
