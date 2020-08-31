package at.whattudo.events.subscription;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubscriptionDeleteEvent extends SubscriptionEvent {
    public SubscriptionDeleteEvent(String name) {
        super(name);
    }
}
