package at.whattudo.events.location;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class LocationEvent extends CustomEvent {
    public LocationEvent(String elementName) {
        super(elementName);
    }
}
