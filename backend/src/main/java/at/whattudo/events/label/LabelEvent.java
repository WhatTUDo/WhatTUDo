package at.whattudo.events.label;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class LabelEvent extends CustomEvent {
    public LabelEvent(String elementName) {
        super(elementName);
    }
}
