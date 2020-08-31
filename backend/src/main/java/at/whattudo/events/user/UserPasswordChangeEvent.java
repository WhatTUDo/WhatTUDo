package at.whattudo.events.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserPasswordChangeEvent extends UserEvent {
    public UserPasswordChangeEvent(String elementName) {
        super(elementName);
    }
}
