package at.whattudo.events.user;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class UserEvent extends CustomEvent {
    public UserEvent(String elementName) {
        super(elementName);
    }
}
