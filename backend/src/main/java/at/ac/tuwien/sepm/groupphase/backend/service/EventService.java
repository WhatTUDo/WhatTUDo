package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import org.hibernate.criterion.Example;

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


    /**
     * Add labels to an event.
     *
     * @param event  - to add the labels to
     * @param labels to be added to this event
     * @return the updated event
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event addLabels(Event event, Collection<Label> labels);

    /**
     * Remove labels from an event.
     *
     * @param event  - to remove the labels from
     * @param labels to be removed from this event
     * @return the updated event
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event removeLabels(Event event, Collection<Label> labels);

    /**
     * Update labels from an event. Set those as the events labels
     *
     * @param event  - to set the labels to
     * @param labels to be set for this event
     * @return the updated event
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event updateLabels(Event event, Collection<Label> labels);

    List<Event> getByCalendarId(Integer id) throws ServiceException;


}