package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import org.hibernate.criterion.Example;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {

    /**
     * Deletes a single event from the db.
     *
     * @param event event to be deleted from db
     * @throws org.hibernate.service.spi.ServiceException                    if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - id == null;
     *                                                                       - TODO IllegalArgument + IvalidaDataAccess
     */
    void delete(Event event);

    /**
     * Save a new event into the database
     *
     * @param event event to be saved into database .
     * @return the new event.
     * @throws org.hibernate.service.spi.ServiceException                    if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - name of event is empty;
     *                                                                       - startDateTime is after endDateTime;
     */
    Event save(Event event);

    /**
     * Find a single event by id.
     *
     * @param id id of the event entry.
     * @return the event entry
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    Event findById(int id);

    /**
     * Find events by name.
     *
     * @param name name (or part of a name) for which a search will be performed.
     * @return List of event entities that match the search-name
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    List<Event> findByName(String name);

    /**
     * Find all Events with a start-date between two dates.
     *
     * @param start earliest start-date of the event.
     * @param end   latest start-date of the event.
     * @return list of all event entries that have a start-date between start and end.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no events are between start and end
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     */
    List<Event> findForDates(LocalDateTime start, LocalDateTime end);

    /**
     * Update a single event in the db with new values
     *
     * @param event event to be updated into database with the new values.
     * @return the updated event entry.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in db
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     *                                                                          - TODO IllegalArgument + IvalidaDataAccess
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException    if:
     *                                                                          - name is empty
     *                                                                          - startDateTime is in the past
     *                                                                          - startDateTime is after endDateTime;
     */
    Event update(Event event);

    /**
     * Add labels to an event.
     *
     * @param event - to add the labels to
     * @param labels  to be added to this event
     * @return the updated event
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event addLabels(Event event, Collection<Label> labels);

    /**
     * Remove labels from an event.
     *
     * @param event - to remove the labels from
     * @param labels    to be removed from this event
     * @return the updated event
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event removeLabels(Event event, Collection<Label> labels);

}