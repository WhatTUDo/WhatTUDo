package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatusPossibilities;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotAllowedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public AttendanceStatus create(AttendanceStatus attendanceStatus) throws ServiceException {
        try {
            if (attendanceStatus.getEvent().getEndDateTime().isBefore(LocalDateTime.now())) {
                throw new NotAllowedException("Sorry, this event is over!");
            }
            List<AttendanceStatus> list = attendanceRepository.getByUser(attendanceStatus.getUser());
            if (!list.isEmpty()) {
                for (AttendanceStatus a : list) {
                    if (a.getEvent().getId().equals(attendanceStatus.getEvent().getId())) {
                        attendanceRepository.delete(a);

                    }
                }

            }
            return attendanceRepository.save(attendanceStatus);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<ApplicationUser> getUsersByEvent(Event event) throws ServiceException {
        try {
            List<AttendanceStatus> list = attendanceRepository.getByEvent(event);
            List<ApplicationUser> users = new ArrayList<>();
            for (AttendanceStatus a : list) {
                users.add(a.getUser());
            }
            return users;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Event> getEventByUser(ApplicationUser user) throws ServiceException {
        try {
            List<AttendanceStatus> list = attendanceRepository.getByUser(user);
            List<Event> events = new ArrayList<>();
            for (AttendanceStatus a : list) {
                events.add(a.getEvent());
            }
            return events;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public List<Event> getEventUserIsAttending(Integer userId) throws ServiceException, NotFoundException {
        try {
            Optional<ApplicationUser> userFound = userRepository.findById(userId);
            if (!userFound.isPresent()) {
                throw new NotFoundException("user not found");
            }
            List<AttendanceStatus> list = attendanceRepository.getByUser(userFound.get());
            List<Event> events = new ArrayList<>();
            for (AttendanceStatus a : list) {
                if (a.getStatus().equals(AttendanceStatusPossibilities.ATTENDING)) {
                    events.add(a.getEvent());
                }
            }
            return events;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Event> getEventUserIsInterested(Integer userId) throws ServiceException, NotFoundException {
        try {
            Optional<ApplicationUser> userFound = userRepository.findById(userId);
            if (!userFound.isPresent()) {
                throw new NotFoundException("user not found");
            }
            List<AttendanceStatus> list = attendanceRepository.getByUser(userFound.get());
            List<Event> events = new ArrayList<>();
            for (AttendanceStatus a : list) {
                if (a.getStatus().equals(AttendanceStatusPossibilities.INTERESTED)) {
                    events.add(a.getEvent());
                }
            }
            return events;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationUser> getUsersAttendingEvent(Integer eventId) throws ServiceException, NotFoundException {
        try {
            Optional<Event> eventFound = eventRepository.findById(eventId);
            if (!eventFound.isPresent()) {
                throw new NotFoundException("event not found");
            }
            List<AttendanceStatus> list = attendanceRepository.getByEvent(eventFound.get());
            List<ApplicationUser> users = new ArrayList<>();
            for (AttendanceStatus a : list) {
                if (a.getStatus().equals(AttendanceStatusPossibilities.ATTENDING)) {
                    users.add(a.getUser());
                }
            }
            return users;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationUser> getUsersInterestedInEvent(Integer eventId) throws ServiceException, NotFoundException {
        try {
            Optional<Event> eventFound = eventRepository.findById(eventId);
            if (!eventFound.isPresent()) {
                throw new NotFoundException("event not found");
            }
            List<AttendanceStatus> list = attendanceRepository.getByEvent(eventFound.get());
            List<ApplicationUser> users = new ArrayList<>();
            for (AttendanceStatus a : list) {
                if (a.getStatus().equals(AttendanceStatusPossibilities.INTERESTED)) {
                    users.add(a.getUser());
                }
            }
            return users;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationUser> getUsersDecliningEvent(Integer eventId) throws ServiceException, NotFoundException {
        try {
            Optional<Event> eventFound = eventRepository.findById(eventId);
            if (!eventFound.isPresent()) {
                throw new NotFoundException("event not found");
            }
            List<AttendanceStatus> list = attendanceRepository.getByEvent(eventFound.get());
            List<ApplicationUser> users = new ArrayList<>();
            for (AttendanceStatus a : list) {
                if (a.getStatus().equals(AttendanceStatusPossibilities.DECLINED)) {
                    users.add(a.getUser());
                }
            }
            return users;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteStatus(Integer id) throws ServiceException {
        try {
            attendanceRepository.deleteById(id);
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public AttendanceStatus getStatus(Integer userId, Integer eventId) throws ServiceException {
        try {
            Optional<ApplicationUser> applicationUser = userRepository.findById(userId);
            if (applicationUser.isEmpty()) {
                throw new NotFoundException("User Not Found!");
            }
            List<AttendanceStatus> list = attendanceRepository.getByUser(applicationUser.get());
            for (AttendanceStatus a : list) {
                if (a.getEvent().getId().equals(eventId)) {
                    return a;
                }
            }
            return null;
        } catch (PersistenceException |IllegalArgumentException e){
            throw new ServiceException(e.getMessage());
        }
    }



}
