package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventCollisionService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleEventCollisionService implements EventCollisionService {

    private final EventService eventService;

    private final LabelService labelService;
    List<EventCollision> eventCollisions = new ArrayList<>();

    @Override
    public List<EventCollision> getEventCollisions(Event event, Integer scoreThreshold, Long additionalTimespan) throws ServiceException, ValidationException {
        try {
            LocalDateTime start = event.getStartDateTime().minusHours(additionalTimespan);
            LocalDateTime end = event.getEndDateTime().plusHours(additionalTimespan);
            List<Event> events = this.eventService.findForDates(start, end); // get all Dates with a possible overlap.

            for (Event collidingEvent : events) {
                if (!collidingEvent.getId().equals(event)) {
                    Integer score = this.getCollisionScore(event, collidingEvent);
                    if (score > scoreThreshold) {
                        eventCollisions.add(new EventCollision(event, score, "Found a collision!"));
                    }
                }

            }
            return eventCollisions;
        } catch (NotFoundException e) {
            log.warn("No Dates for Collision Detection found, caught NotFoundException: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private Integer getCollisionScore(Event newEvent, Event collidingEvent) {
        Integer score = 0;
        //Fixme: Needs to be re-activated once the Lazy Init Bug in DB is fixed.
        //score += this.compareLabels(newEvent, collidingEvent);
        score += this.compareDates(newEvent, collidingEvent);
        return score;
    }

    private Integer compareLabels(Event newEvent, Event collidingEvent) {
        Integer labelScore = 0;
        List<Label> newEventLabels = newEvent.getLabels();
        if (newEventLabels == null) newEventLabels = new ArrayList<>();
        List<Label> collidingEventLabels = labelService.findByEventId(collidingEvent.getId());

        for (Label label : newEventLabels) {
            if (collidingEventLabels.contains(label)) labelScore += 2;
        }

        return labelScore;
    }

    private Integer compareDates(Event newEvent, Event collidingEvent) {
        LocalDateTime startA = newEvent.getStartDateTime();
        LocalDateTime endA = newEvent.getEndDateTime();
        LocalDateTime startB = collidingEvent.getStartDateTime();
        LocalDateTime endB = collidingEvent.getStartDateTime();

        //Case 0: No Overlap
        if (isBeforeOrEqual(endA, startB)) {
            return 0;
        }
        //case 1: Overlap A before B
        else if (isBeforeOrEqual(startA, startB) && isAfterOrEqual(endA, startB)) {
            Double overlapAmount = this.getAmountOfOverlap(newEvent, collidingEvent);
            return calculateScore(overlapAmount);
        }

        //case 2: Overlap B before A
        else if (isBeforeOrEqual(startB, startA) && isBeforeOrEqual(startA, endB)) {
            Double overlapAmount = this.getAmountOfOverlap(collidingEvent, newEvent);
            return calculateScore(overlapAmount);
        }

        //case 3: Total Overlap
        else if (isAfterOrEqual(startA, startB) && isBeforeOrEqual(endA, endB)) {
            return 5;
        } else {
            throw new ServiceException("Cannot determine Event Date Overlap!");
        }

    }

    private Double getAmountOfOverlap(Event earlierEvent, Event laterEvent) {
        long timeOfEarlierEventInSeconds = earlierEvent.getStartDateTime().until(earlierEvent.getEndDateTime(), ChronoUnit.SECONDS);
        long amountOfSecondsInOverlap = laterEvent.getStartDateTime().until(earlierEvent.getEndDateTime(), ChronoUnit.SECONDS);

        return ((double) amountOfSecondsInOverlap / (double) timeOfEarlierEventInSeconds) * 100;
    }

    private Integer calculateScore(Double overlapAmount) {
        int overlapInInt = overlapAmount.intValue();

        if (overlapInInt > 50) {
            if (overlapInInt <= 60) {
                return 1;
            }
            if (overlapInInt <= 70) {
                return 2;
            }
            if (overlapInInt <= 80) {
                return 3;
            }
            if (overlapInInt <= 90) {
                return 4;
            }
            if (overlapInInt <= 100) {
                return 5;
            }
        } else if (overlapAmount > 100) {
            throw new ServiceException("Overlap Amount cannot exceed 100%");
        }

        return 0;
    }

    //date helper methods.
    private boolean isBeforeOrEqual(LocalDateTime time1, LocalDateTime time2) {
        return time1.isBefore(time2) || time1.isEqual(time2);
    }

    private boolean isAfterOrEqual(LocalDateTime time1, LocalDateTime time2) {
        return time1.isAfter(time2) || time1.isEqual(time2);
    }

    @Override
    public List<LocalDateTime[]> getAlternativeDateSuggestions(Event event, Integer initialScore) throws ServiceException, ValidationException {
        List<LocalDateTime[]> rec = new ArrayList<>();
        Event help = new Event(event.getName(), event.getStartDateTime(), event.getEndDateTime(), event.getCalendar());
        for (int i = 1; i < 4; i++) {
            help.setStartDateTime(event.getStartDateTime().plusDays(i));
            help.setEndDateTime(event.getEndDateTime().plusDays(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
            help.setStartDateTime(event.getStartDateTime().minusDays(i));
            help.setStartDateTime(event.getEndDateTime().minusDays(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
        }

        for (int i = 1; i < 3; i++) {
            help.setStartDateTime(event.getStartDateTime().plusWeeks(i));
            help.setEndDateTime(event.getEndDateTime().plusWeeks(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
            help.setStartDateTime(event.getStartDateTime().minusWeeks(i));
            help.setStartDateTime(event.getEndDateTime().minusWeeks(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
        }

        for (int i = 1; i < 3; i++) {
            help.setStartDateTime(event.getStartDateTime().plusHours(i));
            help.setEndDateTime(event.getEndDateTime().plusHours(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
            help.setStartDateTime(event.getStartDateTime().minusHours(i));
            help.setStartDateTime(event.getEndDateTime().minusHours(i));
            if (getEventCollisions(help, initialScore, 12L).isEmpty()) {
                rec.add(new LocalDateTime[]{help.getStartDateTime(), help.getEndDateTime()});
            }
        }
        for (EventCollision e:eventCollisions) {
            if(e.getCollisionScore() < 2){
                rec.add(new LocalDateTime[]{e.getEvent().getStartDateTime(), e.getEvent().getEndDateTime()});
            }
        }
        return rec;
    }


}
