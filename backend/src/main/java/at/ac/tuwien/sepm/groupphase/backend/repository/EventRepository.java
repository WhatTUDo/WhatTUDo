package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Find all events in the db which belong to a certain calendar having its id stored.
     *
     * @return list of all event entries in this calendar
     */
    List<Event> findByCalendarId(Integer calendar_id);

    List<Event> findAllByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findAllByNameContains(String nameString);

}
