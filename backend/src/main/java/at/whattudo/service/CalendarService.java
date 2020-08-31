package at.whattudo.service;

import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.exception.NotFoundException;
import at.whattudo.util.ValidationException;
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
     * Updates a Calendar with the given Organization Entities. Also updates the Calendar data for Organization items.
     *
     * @param calendar      calendar to be updated with new organizations
     * @param organizations List of organizations the calendar is managed by.
     * @return the updated calendar-entity
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Calendar Entity does not pass validation.
     * @throws NotFoundException   is thrown if the Calendar Entity Id is not found in Database.
     */
    Calendar updateOrganizationsWithList(Calendar calendar, List<Organization> organizations) throws ServiceException, NotFoundException, ValidationException;

    /**
     * Sets the cover image
     *
     * @param calendar  to set the cover to
     * @param imageBlob - image blob
     * @return the changed calendar
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Calendar setCoverImage(Calendar calendar, byte[] imageBlob);
}
