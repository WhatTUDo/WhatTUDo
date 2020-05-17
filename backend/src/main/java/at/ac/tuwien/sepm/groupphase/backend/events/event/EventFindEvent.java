package at.ac.tuwien.sepm.groupphase.backend.events.event;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EventFindEvent extends EventEvent {
    public EventFindEvent(String name) {
        super(name);
    }
}
