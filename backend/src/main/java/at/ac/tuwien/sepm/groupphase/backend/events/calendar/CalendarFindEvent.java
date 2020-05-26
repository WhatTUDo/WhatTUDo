package at.ac.tuwien.sepm.groupphase.backend.events.calendar;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CalendarFindEvent extends CalendarEvent {
    public CalendarFindEvent(String name) {
        super(name);
    }
}
