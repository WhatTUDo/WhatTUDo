package at.ac.tuwien.sepm.groupphase.backend.events.user;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class UserEvent extends CustomEvent {
    public UserEvent(String elementName) {
        super(elementName);
    }
}
