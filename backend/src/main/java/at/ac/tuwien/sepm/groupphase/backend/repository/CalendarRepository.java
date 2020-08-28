package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

    /**
     * Find all calendars in the db where the calendar-name contains a specific name/sequence of characters.
     *
     * @param nameString String to be tested against all calendar-names if these contain nameString.
     * @return list of all calendar entries that contain nameString.
     */
    List<Calendar> findAllByNameContainingIgnoreCase(String nameString);

}
