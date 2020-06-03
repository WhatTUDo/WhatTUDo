package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface AttendanceService {
    /**
     * Create a Attendance entity
     *
     * @param attendanceStatus status that is going to be created or updated
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    AttendanceStatus create(AttendanceStatus attendanceStatus) throws ServiceException;


    /**
     * Get all users that responded to event
     *
     * @param event Event whose users we want to get.
     * @throws ServiceException is thrown if something goes wrong during data processing.
     * @return list of users that responded to event.
     */
    List<ApplicationUser> getUsersByEvent(Event event) throws ServiceException;

    /**
     * Get all events that user responded to
     *
     * @param user User whose events we want to get.
     * @throws ServiceException is thrown if something goes wrong during data processing.
     * @return list of events that user responded to.
     */
    List<Event> getEventByUser(ApplicationUser user) throws ServiceException;

     List<Event> getEventUserIsAttending(Integer userId) throws ServiceException, NotFoundException;

     List<Event> getEventUserIsInterested(Integer userId) throws ServiceException, NotFoundException;

    List<ApplicationUser> getUsersAttendingEvent(Integer eventId) throws ServiceException, NotFoundException;

    List<ApplicationUser> getUsersInterestedInEvent(Integer eventId) throws ServiceException, NotFoundException;
    List<ApplicationUser> getUsersDecliningEvent(Integer eventId) throws ServiceException, NotFoundException;
}
