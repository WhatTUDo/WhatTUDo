package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.EventCollision;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventCollisionService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import io.swagger.models.auth.In;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleEventCollisionService implements EventCollisionService {

    private final EventService eventService;

    private final LabelService labelService;

    @Override
    public List<EventCollision> getEventCollisions(Event event, Integer scoreThreshold, Long additionalTimespan) throws ServiceException, ValidationException {
        try {
            List<EventCollision> eventCollisions = new ArrayList<>();
            LocalDateTime start = event.getStartDateTime().minusHours(additionalTimespan);
            LocalDateTime end = event.getEndDateTime().plusHours(additionalTimespan);
            List<Event> events = this.eventService.findForDates(start, end); // get all Dates with a possible overlap.

            for (Event collidingEvent : events) {
                if (!collidingEvent.getId().equals(event.getId())) {
                    Integer score = this.getCollisionScore(event, collidingEvent);
                    if (score >= scoreThreshold) {
                        eventCollisions.add(new EventCollision(collidingEvent, score, "Found a collision!"));
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
        score += this.compareLabels(newEvent, collidingEvent);
        score += this.compareDates(newEvent, collidingEvent);
        return score;
    }

    private Integer compareLabels(Event newEvent, Event collidingEvent) {
        Integer labelScore = 0;
        List<Label> newEventLabels = newEvent.getLabels();
        if (newEventLabels == null) newEventLabels = new ArrayList<>();
        List<Label> collidingEventLabels = labelService.findByEventId(collidingEvent.getId());

        for (Label label : newEventLabels) {
            for (Label collidingLabel : collidingEventLabels) {
                if (collidingLabel.getId().equals(label.getId())) labelScore += 2;
            }
        }

        return labelScore;
    }

    private Integer compareDates(Event newEvent, Event collidingEvent) {
        LocalDateTime startA = newEvent.getStartDateTime();
        LocalDateTime endA = newEvent.getEndDateTime();
        LocalDateTime startB = collidingEvent.getStartDateTime();
        LocalDateTime endB = collidingEvent.getEndDateTime();
        //Case 0: No Overlap
        if (isBeforeOrEqual(endA, startB)) {
            return 0;
        } else if (isAfterOrEqual(startA, endB)) {
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
        Map<LocalDateTime[], Integer> rec = new HashMap<>();
        Event help = new Event(event.getName(), event.getStartDateTime(), event.getEndDateTime(), event.getCalendar());
        for (int i = 1; i < 3; i++) {
            if (event.getStartDateTime().getHour() < 22) {
                help.setStartDateTime(event.getStartDateTime().plusHours(i));
                help.setEndDateTime(event.getEndDateTime().plusHours(i));
                if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                    recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
                }
            }

            if (event.getStartDateTime().getHour() > 8) {
                help.setStartDateTime(event.getStartDateTime().minusHours(i));
                help.setEndDateTime(event.getEndDateTime().minusHours(i));
                if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                    recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
                }
            }
        }
        for (int i = 1; i < 7; i++) {
            help.setStartDateTime(event.getStartDateTime().plusDays(i));
            help.setEndDateTime(event.getEndDateTime().plusDays(i));
            if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
            }
            help.setStartDateTime(event.getStartDateTime().minusDays(i));
            help.setEndDateTime(event.getEndDateTime().minusDays(i));
            if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
            }
        }

        for (int i = 1; i < 3; i++) {
            help.setStartDateTime(event.getStartDateTime().plusWeeks(i));
            help.setEndDateTime(event.getEndDateTime().plusWeeks(i));
            if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
            }
            help.setStartDateTime(event.getStartDateTime().minusWeeks(i));
            help.setEndDateTime(event.getEndDateTime().minusWeeks(i));
            if (help.getStartDateTime().isAfter(LocalDateTime.now())) {
                recommendationLookup(getEventCollisions(help, initialScore, 12L), help, rec);
            }
        }

        if (rec.isEmpty()) {
            throw new ServiceException("Unfortunately we couldn't find any suggestions.");
        }
        return filterBestRecommendations(rec);
    }


    public Map<LocalDateTime[], Integer> recommendationLookup(List<EventCollision> eventCollisions, Event solution, Map<LocalDateTime[], Integer> rec) {
        if (eventCollisions.isEmpty()) {
            rec.put(new LocalDateTime[]{solution.getStartDateTime(), solution.getEndDateTime()}, 0);
        } else {
            EventCollision max = eventCollisions.stream().max(Comparator.comparing(e -> e.getCollisionScore())).get();
            rec.put(new LocalDateTime[]{max.getEvent().getStartDateTime(), max.getEvent().getEndDateTime()}, max.getCollisionScore());
        }
        return rec;
    }

    public List<LocalDateTime[]> filterBestRecommendations(Map<LocalDateTime[], Integer> rec) {
        List<LocalDateTime[]> best = filterMap(rec, 0);

        rec.values().removeIf(value -> value == 0);
        if (rec.isEmpty()) {
            return best;
        }
        Integer min = Collections.min(rec.values());

        List<LocalDateTime[]> good = filterMap(rec, min);

        return Stream.concat(best.stream(), good.stream())
            .collect(Collectors.toList());

    }

    public List<LocalDateTime[]> filterMap(Map<LocalDateTime[], Integer> rec, int min) {
        return rec.entrySet().stream()
            .filter(x -> x.getValue() == min)
            .map(map -> map.getKey())
            .collect(Collectors.toList());
    }
}
