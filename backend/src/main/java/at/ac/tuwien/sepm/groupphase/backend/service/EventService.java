package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;

import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {

    /**
     * Deletes a single event from the db.
     *
     * @param id of event to be deleted from db
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Event Entity does not pass validation.
     */
    void delete(Integer id) throws ServiceException, ValidationException;

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


    /**
     * Add labels to an event.
     *
     * @param event  - to add the labels to
     * @param labels to be added to this event
     * @return the updated event
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Event addLabels(Event event, Collection<Label> labels) throws ServiceException;

    /**
     * Remove labels from an event.
     *
     * @param event  - to remove the labels from
     * @param labels to be removed from this event
     * @return the updated event
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Event removeLabels(Event event, Collection<Label> labels) throws ServiceException;

    /**
     * Update labels from an event. Set those as the events labels
     *
     * @param event  - to set the labels to
     * @param labels to be set for this event
     * @return the updated event
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Event updateLabels(Event event, Collection<Label> labels) throws ServiceException;

    /**
     * Performs a text search and returns all Events that match the search term.
     *
     * @param searchterm: String for which the search is performed.
     * @return List of Events whose Name or Description properties contain the searchterm String.
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    List<Event> findNameOrDescriptionBySearchTerm(String searchterm) throws ServiceException;


    /**
     * Returns a List of Events belonging to a Calendar with the given Id.
     *
     * @param id id of the calendar
     * @return List of all Events in the calendar
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    List<Event> getByCalendarId(Integer id) throws ServiceException;

    /**
     * Sets the cover image of this event
     *
     * @param event     to set the image to
     * @param imageBlob as byte array
     * @return the updated event
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Event setCoverImage(Event event, byte[] imageBlob) throws ServiceException;
}