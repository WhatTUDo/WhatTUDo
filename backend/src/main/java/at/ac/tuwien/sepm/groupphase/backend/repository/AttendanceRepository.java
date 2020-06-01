package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<ApplicationStatus, Integer> {
    /**
     * Find all event that user will be attending/declining or is interested  in.
     *
     * @param user User to be tested against all status-users if these contain user.
     * @return list of all statuses that contain user.
     */
    List<ApplicationStatus> getByUser(ApplicationUser user);

    /**
     * Find all users that responded to event.
     *
     * @param event Event to be tested against all status-events if these contain event.
     * @return list of all statuses that contain event.
     */
    List<ApplicationStatus> getByEvent(Event event);


}
