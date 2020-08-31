package at.whattudo.events.organization;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public abstract class OrganizationCalendarEvent extends OrganizationEvent {
    @Getter
    private final Collection<String> calendarNames;

    public OrganizationCalendarEvent(String elementName, Collection<String> calendarNames) {
        super(elementName);
        this.calendarNames = calendarNames;
    }
}
