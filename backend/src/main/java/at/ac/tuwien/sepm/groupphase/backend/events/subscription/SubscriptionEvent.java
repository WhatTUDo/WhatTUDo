package at.ac.tuwien.sepm.groupphase.backend.events.subscription;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class SubscriptionEvent extends CustomEvent {
    public SubscriptionEvent(String elementName) {
        super(elementName);
    }
}
