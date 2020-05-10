package at.ac.tuwien.sepm.groupphase.backend.events.event;

import org.springframework.context.ApplicationEvent;

public class EventCreateEvent extends ApplicationEvent {
    public EventCreateEvent(String name) {
        super(name);
    }
}
