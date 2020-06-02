package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Override
    public AttendanceStatus create(AttendanceStatus attendanceStatus) throws ServiceException {
        try {
            List<AttendanceStatus> list = attendanceRepository.getByUser(attendanceStatus.getUser());
            if (list.contains(attendanceStatus.getEvent())) {
                attendanceRepository.delete(attendanceStatus);

            }
            return attendanceRepository.save(attendanceStatus);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<ApplicationUser> getUsersByEvent(Event event) throws ServiceException {
        List<AttendanceStatus> list = attendanceRepository.getByEvent(event);
        List<ApplicationUser> users = new ArrayList<>();
        for (AttendanceStatus a: list) {
            users.add(a.getUser());
        }
        return users;
    }

    @Override
    public List<Event> getEventByUser(ApplicationUser user) throws ServiceException {
        List<AttendanceStatus> list = attendanceRepository.getByUser(user);
        List<Event> events = new ArrayList<>();
        for (AttendanceStatus a: list) {
            events.add(a.getEvent());
        }
        return events;
    }
}
