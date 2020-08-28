package at.ac.tuwien.sepm.groupphase.backend.events.event;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventCreateEvent extends EventEvent {
    public EventCreateEvent(String name) {
        super(name);
    }
}
