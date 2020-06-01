package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Override
    public ApplicationStatus create(ApplicationStatus applicationStatus) throws ServiceException {
        try {
            List<ApplicationStatus> list = attendanceRepository.getByUser(applicationStatus.getUser());
            if (list.contains(applicationStatus.getEvent())) {
                attendanceRepository.delete(applicationStatus);

            }
            return attendanceRepository.save(applicationStatus);
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<ApplicationUser> getUsersByEvent(Event event) throws ServiceException {
        return null;
    }

    @Override
    public List<Event> getEventByUser(ApplicationUser user) throws ServiceException {
        return null;
    }
}
