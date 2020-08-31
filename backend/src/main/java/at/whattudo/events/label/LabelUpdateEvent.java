package at.whattudo.events.label;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LabelUpdateEvent extends LabelEvent {
    public LabelUpdateEvent(String name) {
        super(name);
    }
}
