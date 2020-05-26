package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface CalendarService {


    /**
     * Delete a single calendar from the db.
     *
     * @param id of the calendar to be deleted.
     * @throws org.hibernate.service.spi.ServiceException                    if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - id == null;
     *                                                                       - TODO IA/IDA
     */
    void delete(Integer id);

    /**
     * Update a calendar in the db with new values
     *
     * @param calendar to be updated into database with new values.
     * @return the updated calendar entry.
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if calendar does not pass validation.
     */
    Calendar update(Calendar calendar);

    /**
     * Finds a single calendar by id
     *
     * @param id id of the calendar entry
     * @return the calendar entity
     * @throws NotFoundException is thrown when the Calendar Entity with the specified ID is not in DB
     * @throws ServiceException  is thrown when some errors occur during the database query.
     */
    Calendar findById(Integer id) throws NotFoundException, ServiceException;

    /**
     * Finds all available Calendar Entities
     *
     * @return List of Calendar Entities
     * @throws NotFoundException is thrown when no Calendar Entities are found.
     * @throws ServiceException  is thrown when some errors occur during the database query.
     */
    List<Calendar> findAll() throws NotFoundException, ServiceException;

    /**
     * Save a new calendar into the db.
     *
     * @param calendar calendar to saved (depending on given ID)
     * @return the save calendar entry
     * @throws NotFoundException   is thrown when the Calendar Entity with the specified ID is not in DB
     * @throws ServiceException    is thrown when some errors occur during the database query.
     * @throws ValidationException is thrown when the given Calendar does not pass validation.
     */
    Calendar save(Calendar calendar) throws NotFoundException, ServiceException, ValidationException;

    /**
     * Find calendars by name
     *
     * @param name name (or part of a name) for which a search will be performed.
     * @return List of calendar entities that match the search-name
     * @throws NotFoundException is thrown when no Calendar Entities are found.
     * @throws ServiceException  is thrown when some errors occur during the database query.
     */
    List<Calendar> findByName(String name) throws ServiceException;
}
