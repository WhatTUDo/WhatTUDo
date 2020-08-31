package at.whattudo.events.event;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class EventEvent extends CustomEvent {
    public EventEvent(String elementName) {
        super(elementName);
    }
}
