package at.whattudo.events.event;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventDeleteEvent extends EventEvent {
    public EventDeleteEvent(String eventName) {
        super(eventName);
    }
}
