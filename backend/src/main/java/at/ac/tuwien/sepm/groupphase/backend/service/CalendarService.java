package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface CalendarService {


    /**
     * Delete a single calendar from the db.
     *
     * @param id of the calendar to be deleted
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Calendar Entity does not pass validation.
     */
    void delete(Integer id) throws ServiceException, ValidationException;

    /**
     * Update a calendar in the db with new values.
     *
     * @param calendar to be updated into database with new values
     * @return the updated calendar entry
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Calendar Entity does not pass validation.
     */
    Calendar update(Calendar calendar) throws ServiceException, ValidationException;

    /**
     * Finds a single calendar by id.
     *
     * @param id id of the calendar entry
     * @return the calendar entity
     * @throws NotFoundException is thrown if no calendar with specified id can be found in db.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     */
    Calendar findById(Integer id) throws NotFoundException, ServiceException;

    /**
     * Finds all available Calendar Entities.
     *
     * @return List of Calendar Entities
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Calendar> findAll() throws ServiceException;

    /**
     * Save a new calendar into the db.
     *
     * @param calendar calendar to saved
     * @return the save calendar entry
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Calendar Entity does not pass validation.
     */
    Calendar save(Calendar calendar) throws ServiceException, ValidationException;

    /**
     * Find calendars by name.
     *
     * @param name name (or part of a name) for which a search will be performed
     * @return List of calendar entities that match the search-name
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Calendar> findByName(String name) throws ServiceException;

    /**
     * TODO: Updates a Calendar with the given Organization Entities. Also updates the Calendar data for Organization items.
     * @param calendar
     * @param organizations
     * @return
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Calendar Entity does not pass validation.
     * @throws NotFoundException   is thrown if the Calendar Entity Id is not found in Database.
     */
    Calendar updateOrganizationsWithList(Calendar calendar, List<Organization> organizations) throws ServiceException, NotFoundException, ValidationException;
}
