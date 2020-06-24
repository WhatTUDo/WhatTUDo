package at.ac.tuwien.sepm.groupphase.backend.events.subscription;

import at.ac.tuwien.sepm.groupphase.backend.events.label.LabelEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubscriptionDeleteEvent extends SubscriptionEvent {
    public SubscriptionDeleteEvent(String name) {
        super(name);
    }
}
