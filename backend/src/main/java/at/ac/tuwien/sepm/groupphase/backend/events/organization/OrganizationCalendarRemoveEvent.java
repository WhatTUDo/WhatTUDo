package at.ac.tuwien.sepm.groupphase.backend.events.organization;

import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
public class OrganizationCalendarRemoveEvent extends OrganizationCalendarEvent {
    public OrganizationCalendarRemoveEvent(String elementName, Collection<String> calendarNames) {
        super(elementName, calendarNames);
    }

    @Override
    public String getMessage() {
        return String.format("Removing calendars %s from organization %s", getCalendarNames(), getElementName());
    }
}
