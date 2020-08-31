package at.whattudo.events.label;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LabelCreateEvent extends LabelEvent {
    public LabelCreateEvent(String name) {
        super(name);
    }
}
