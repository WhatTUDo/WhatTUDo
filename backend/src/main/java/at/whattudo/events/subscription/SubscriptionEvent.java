package at.whattudo.events.subscription;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class SubscriptionEvent extends CustomEvent {
    public SubscriptionEvent(String elementName) {
        super(elementName);
    }
}
