package at.ac.tuwien.sepm.groupphase.backend.events.calendar;

import at.ac.tuwien.sepm.groupphase.backend.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class CalendarEvent extends CustomEvent {
    public CalendarEvent(String elementName) {
        super(elementName);
    }
}
