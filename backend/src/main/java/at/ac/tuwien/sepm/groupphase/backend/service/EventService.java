package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Deletes a single event from the db.
     *
     * @param event event to be deleted from db
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Event Entity does not pass validation.
     */
    void delete(Event event) throws ServiceException, ValidationException;

    /**
     * Save a new event into the database.
     *
     * @param event event to be saved into database
     * @return the new event
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Event Entity does not pass validation.
     */
    Event save(Event event) throws ServiceException, ValidationException;

    /**
     * Find a single event by id.
     *
     * @param id id of the event entry
     * @return the event entry
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException is thrown if no event with specified id can be found in db.
     */
    Event findById(int id) throws ServiceException, NotFoundException;

    /**
     * Find events by name.
     *
     * @param name name (or part of a name) for which a search will be performed
     * @return List of event entities that match the search-name
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Event> findByName(String name) throws ServiceException;

    /**
     * Find all Events with a start-date between two dates.
     *
     * @param start earliest start-date of the event
     * @param end   latest start-date of the event
     * @return list of all event entries that have a start-date between start and end
     * @throws NotFoundException is thrown if no events are between start and end.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     */
    List<Event> findForDates(LocalDateTime start, LocalDateTime end) throws NotFoundException, ServiceException;

    /**
     * Update a single event in the db with new values.
     *
     * @param event event to be updated into database with the new values
     * @return the updated event entry
     * @throws NotFoundException   is thrown if no event can be found in db.
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Event Entity does not pass validation.
     */
    Event update(Event event) throws NotFoundException, ServiceException, ValidationException;

    List<Event> getByCalendarId(Integer id) throws ServiceException;

}