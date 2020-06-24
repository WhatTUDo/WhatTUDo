package at.ac.tuwien.sepm.groupphase.backend.events.subscription;

import at.ac.tuwien.sepm.groupphase.backend.events.label.LabelEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubscriptionCreateEvent extends SubscriptionEvent {
    public SubscriptionCreateEvent(String name) {
        super(name);
    }
}
