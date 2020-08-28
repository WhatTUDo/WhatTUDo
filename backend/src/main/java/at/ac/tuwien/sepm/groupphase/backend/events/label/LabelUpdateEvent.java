package at.ac.tuwien.sepm.groupphase.backend.events.label;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class LabelUpdateEvent extends LabelEvent {
    public LabelUpdateEvent(String name) {
        super(name);
    }
}
