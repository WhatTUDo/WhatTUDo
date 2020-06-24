package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface AttendanceService {
    /**
     * Create a Attendance entity.
     *
     * @param attendanceStatus status that is going to be created or updated
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    AttendanceStatus create(AttendanceStatus attendanceStatus) throws ServiceException;


    /**
     * Get all users that responded to event.
     *
     * @param event Event whose users we want to get.
     * @return list of users that responded to event.
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<ApplicationUser> getUsersByEvent(Event event) throws ServiceException;

    /**
     * Get all events that user responded to.
     *
     * @param user User whose events we want to get.
     * @return list of events that user responded to.
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Event> getEventByUser(ApplicationUser user) throws ServiceException;

    /**
     * Get all events a user is attending.
     *
     * @param userId id of user on which the search will be performed for.
     * @return list of events that user is attending/has attended.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no user with userId can be found in database.
     */
    List<Event> getEventUserIsAttending(Integer userId) throws ServiceException, NotFoundException;

    /**
     * Get all events a user is interested in.
     *
     * @param userId id of user on which the search will be performed for.
     * @return list of events that user is interested in.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no user with userId can be found in database.
     */
    List<Event> getEventUserIsInterested(Integer userId) throws ServiceException, NotFoundException;

    /**
     * Get all Users attending an event.
     *
     * @param eventId id of the event the search will be performed for.
     * @return list of users that are attending the event.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no event with userId can be found in database.
     */
    List<ApplicationUser> getUsersAttendingEvent(Integer eventId) throws ServiceException, NotFoundException;

    /**
     * Get all users interested in an event.
     *
     * @param eventId id of the event the search will be performed for.
     * @return list of users that are interested in the event.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no event with userId can be found in database.
     */
    List<ApplicationUser> getUsersInterestedInEvent(Integer eventId) throws ServiceException, NotFoundException;

    /**
     * Get all users that declined an event.
     *
     * @param eventId id of the event the search will be performed for.
     * @return list of users that have declined the event.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no event with userId can be found in database.
     */
    List<ApplicationUser> getUsersDecliningEvent(Integer eventId) throws ServiceException, NotFoundException;

    /**
     * Delete a AttendanceStatus from the database
     *
     * @param id id of the AttendanceStatuse to be deleted
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    void deleteStatus(Integer id) throws ServiceException;

    /**
     * Get the AttendanceStatus of a specific user and event
     *
     * @param userId  id of the user
     * @param eventId id of the event
     * @return the AttendanceStatus
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException if no event with userId can be found in database.
     */
    AttendanceStatus getStatus(Integer userId, Integer eventId) throws ServiceException, NotFoundException;
}
