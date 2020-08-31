package at.whattudo.events.calendar;

import at.whattudo.events.CustomEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class CalendarEvent extends CustomEvent {
    public CalendarEvent(String elementName) {
        super(elementName);
    }
}
