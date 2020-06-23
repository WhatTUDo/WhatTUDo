package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import biweekly.ICalendar;
import org.hibernate.service.spi.ServiceException;

public interface ICalService {

    //TODO:

    /**
     * @return
     * @throws ServiceException
     */
    ICalendar getAllCalendars() throws ServiceException;

    //TODO:

    /**
     * @param id
     * @return
     * @throws ServiceException
     */
    ICalendar getCalendar(Integer id) throws ServiceException;

    //TODO:

    /**
     * @param user
     * @return
     * @throws ServiceException
     */
    ICalendar getCalendarsForUser(ApplicationUser user) throws ServiceException;
}
