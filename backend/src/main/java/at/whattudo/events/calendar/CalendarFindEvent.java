package at.whattudo.events.calendar;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CalendarFindEvent extends CalendarEvent {
    public CalendarFindEvent(String name) {
        super(name);
    }
}
