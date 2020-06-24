package at.ac.tuwien.sepm.groupphase.backend.events.label;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LabelDeleteEvent extends LabelEvent {
    public LabelDeleteEvent(String name) {
        super(name);
    }
}
