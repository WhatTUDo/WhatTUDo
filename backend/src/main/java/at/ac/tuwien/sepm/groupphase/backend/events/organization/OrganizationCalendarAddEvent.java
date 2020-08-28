package at.ac.tuwien.sepm.groupphase.backend.events.organization;

import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class OrganizationCalendarAddEvent extends OrganizationCalendarEvent {
    public OrganizationCalendarAddEvent(String elementName, Collection<String> calendarNames) {
        super(elementName, calendarNames);
    }

    @Override
    public String getMessage() {
        return String.format("Adding calendars %s to organization %s", getCalendarNames(), getElementName());
    }
}
