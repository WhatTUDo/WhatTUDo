package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import org.hibernate.service.spi.ServiceException;

import javax.validation.ValidationException;
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
     List<EventCollision> getEventCollisions(Event event) throws ServiceException, ValidationException;
}
