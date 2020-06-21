package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import biweekly.ICalendar;
import org.hibernate.service.spi.ServiceException;

public interface ICalService {
    ICalendar getAllCalendars() throws ServiceException;
    ICalendar getCalendar(Integer id) throws ServiceException;
    ICalendar getCalendarsForUser(ApplicationUser user) throws ServiceException;
}
