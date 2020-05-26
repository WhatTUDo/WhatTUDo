package at.ac.tuwien.sepm.groupphase.backend.events.event;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventUpdateEvent extends EventEvent {
    public EventUpdateEvent(String name) {
        super(name);
    }
}
