package at.ac.tuwien.sepm.groupphase.backend.events.label;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class LabelEvent extends CustomEvent {
    public LabelEvent(String elementName) {
        super(elementName);
    }
}
