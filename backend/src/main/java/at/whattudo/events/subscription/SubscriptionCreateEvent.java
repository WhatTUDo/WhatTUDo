package at.whattudo.events.subscription;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SubscriptionCreateEvent extends SubscriptionEvent {
    public SubscriptionCreateEvent(String name) {
        super(name);
    }
}
