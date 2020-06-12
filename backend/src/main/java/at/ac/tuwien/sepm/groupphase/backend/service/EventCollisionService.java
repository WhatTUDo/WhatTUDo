package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import org.hibernate.service.spi.ServiceException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

public interface EventCollisionService {

    /**
     * Gets a List of EventCollision Objects for a given Event.
     * The algorithm for EventCollision generation takes into account:
     * * The associated Labels of the given Event
     * * The StartDateTime and EndDateTime of the given Event
     * * All Events of other calendars that overlap with the given Event.
     *
     * @param event Event entity for which the Collision List should be generated.
     * @return a list of EventCollision Objects, ordered descendingly by their collisionScore.
     * @throws ServiceException is thrown when something goes wrong during calculation.
     * @throws ValidationException is thrown if the Event entity does not pass validation.
     */
    List<EventCollision> getEventCollisions(Event event, Integer scoreThreshold, Long additionalTimespan) throws ServiceException, ValidationException;


    /**
     * Generates start and end dates. Starts with checking the same time up to 6 days after or before initial date of event.
     * Checks if new dates cause collisions by calling getEventCollision. Afterwards calls recommendationLookup
     * and first the method checks if new dates cause no collision and saves new start, end and 0 as collision score to map rec.
     * Second the method recommendationLookup check checks if new date causes collision it adds the max value of score caused with
     * colliding event to map rec.
     * Afterwards it checks same time but up to 2 weeks after or before the initial date and calls getEventCollision and recommendationLookup.
     * In the end it checks same date as initial date of event but different time. It checks up to 2 hours earlier or later.
     * In the end method filterBestRecommendations is called. The method adds to suggestion list the dates that cause no collision(0 score).
     * Afterwards it gets the minimal collision score from map rec (ignoring 0 scores) and adds them to suggestion list.
     * @param event Event entity for which suggestions should be generated
     * @param initialScore minimal collision score event causes.
     * @return list of suggestions with start and end time.
     * @throws ServiceException in case no suggestion are found.
     */
    List<LocalDateTime[]> getAlternativeDateSuggestions(Event event, Integer initialScore) throws ServiceException;
}
