package at.ac.tuwien.sepm.groupphase.backend.events;

import lombok.Data;
import lombok.ToString;

@Data
public abstract class CustomEvent {
    private final String elementName;

    public String getMessage() {
        return String.format("[%s] %s", this.getClass().getSimpleName(), elementName);
    }
}
