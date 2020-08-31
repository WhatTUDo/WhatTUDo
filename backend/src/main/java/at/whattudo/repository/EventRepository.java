package at.whattudo.repository;
import at.whattudo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Find all events in the db which belong to a certain calendar.
     *
     * @param calendar_id id of the calendar, the events belong to.
     * @return list of all event entries in this calendar
     */
    List<Event> findByCalendarId(Integer calendar_id);

    /**
     * Find all events in the db which are scheduled between two points of time.
     *
     * @param start earliest start-date of the event.
     * @param end   latest start-date of the event.
     * @return list of all event entries that have a start-date between start and end.
     */
    List<Event> findAllByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find all events in the db where the event-name contains a specific name/sequence of characters.
     *
     * @param nameString String to be tested against all event-names if these contain nameString.
     * @return list of all event entries that contain nameString.
     */
    List<Event> findAllByNameContains(String nameString);

    /**
     * Searches for a partial match in Name, ignoring the case.
     *
     * @param name search term.
     * @return List of event that match.
     */
    List<Event> findByNameContainingIgnoreCase(String name);

}
