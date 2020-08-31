package at.whattudo.repository;

import at.whattudo.entity.AttendanceStatus;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceStatus, Integer> {

    /**
     * Find all event that user will be attending/declining or is interested  in.
     *
     * @param user User to be tested against all status-users if these contain user.
     * @return list of all statuses that contain user.
     */
    List<AttendanceStatus> getByUser(ApplicationUser user);

    /**
     * Find all users that responded to event.
     *
     * @param event Event to be tested against all status-events if these contain event.
     * @return list of all statuses that contain event.
     */
    List<AttendanceStatus> getByEvent(Event event);

}
