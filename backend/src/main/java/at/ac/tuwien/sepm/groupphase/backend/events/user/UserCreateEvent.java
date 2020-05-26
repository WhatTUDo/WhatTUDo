package at.ac.tuwien.sepm.groupphase.backend.events.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserCreateEvent extends UserEvent {
    public UserCreateEvent(String elementName) {
        super(elementName);
    }
}
