package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import biweekly.ICalendar;
import org.hibernate.service.spi.ServiceException;

public interface ICalService {
    /**
     * Gets all existing calendars as iCal condensed into one calendar
     * @return ICalendar representation
     * @throws ServiceException if the connection to the database fails
     */
    ICalendar getAllCalendars() throws ServiceException;

    /**
     * Gets a calendar as iCal
     * @return ICalendar representation
     * @throws ServiceException if the connection to the database fails
     */
    ICalendar getCalendar(Integer id) throws ServiceException;

    /**
     * Gets all calendars for an user as iCal condensed into one calendar
     * @return ICalendar representation
     * @throws ServiceException if the connection to the database fails
     */
    ICalendar getCalendarsForUser(ApplicationUser user) throws ServiceException;
}
