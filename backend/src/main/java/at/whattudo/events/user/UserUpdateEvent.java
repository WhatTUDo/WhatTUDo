package at.whattudo.events.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserUpdateEvent extends UserEvent {
    public UserUpdateEvent(String elementName) {
        super(elementName);
    }
}
