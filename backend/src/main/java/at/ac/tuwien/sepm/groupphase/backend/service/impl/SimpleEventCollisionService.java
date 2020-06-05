package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.service.EventCollisionService;
import org.hibernate.service.spi.ServiceException;

import javax.validation.ValidationException;
import java.util.List;

public class SimpleEventCollisionService implements EventCollisionService {

    @Override
    public List<EventCollision> getEventCollisions(Event event) throws ServiceException, ValidationException {
        return null;
    }

    private Integer getCollisionScore(Event newEvent, Event collidingEvent) {

        return 0;
    }

    private Integer compareLabels(Event newEvent, Event collidingEvent) {
        Integer labelScore = 0;
        List<Label> newEventLabels = newEvent.getLabels();
        List<Label> collidingEventLabels = collidingEvent.getLabels();

        for (Label label : newEventLabels) {
            if (collidingEventLabels.contains(label)) labelScore += 2;
        }

        return labelScore;
    }

    private Integer compareDates(Event newEvent, Event collidingEvent) {
        Integer dateScore = 0;

        //TODO: Come up with a good idea here.

        return dateScore;
    }

    private Double getEventOverlap(Event eventA, Event eventB) {
        return 0.0;
    }
}
