package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface CalendarService {

    /**
     * Finds and returns 1 Calendar with the specified ID
     * @param id
     * @return Calendar Entity
     * @throws NotFoundException is thrown when the Calendar Entity with the specified ID is not in DB
     * @throws ServiceException is thrown when some errors occur during the database query.
     */
    Calendar findById(Integer id) throws NotFoundException, ServiceException;

    /**
     * Finds all available Calendar Entities
     * @return List of Calendar Entities
     * @throws NotFoundException is thrown when no Calendar Entities are found.
     * @throws ServiceException is thrown when some errors occur during the database query.
     */
    List<Calendar> findAll() throws NotFoundException, ServiceException;

    /**
     *
     * @param calendar Calendar to save / update (depending on given ID)
     * @return Saved / updated Calendar
     * @throws NotFoundException is thrown when the Calendar Entity with the specified ID is not in DB
     * @throws ServiceException is thrown when some errors occur during the database query.
     * @throws ValidationException is thrown when the given Calendar does not pass validation.
     */
    Calendar save(Calendar calendar) throws NotFoundException, ServiceException, ValidationException;

    /**
     *
     * @param name Name (or part of a name) for which a search will be performed.
     * @return List of Calendar Entities
     * @throws NotFoundException is thrown when no Calendar Entities are found.
     * @throws ServiceException is thrown when some errors occur during the database query.
     */
    List<Calendar> findByName(String name) throws NotFoundException, ServiceException;
}
