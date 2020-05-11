package at.ac.tuwien.sepm.groupphase.backend.events.event;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class EventEvent extends CustomEvent {
    public EventEvent(String elementName) {
        super(elementName);
    }
}
