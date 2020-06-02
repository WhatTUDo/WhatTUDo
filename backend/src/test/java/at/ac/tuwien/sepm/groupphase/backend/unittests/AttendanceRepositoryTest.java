package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class AttendanceRepositoryTest {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    EventRepository eventRepository;

    //TODO: fill with real testdata

    @Test
    public void repoBasics() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert der erste Tester", "testmail@supertest.com", "superpasswort"));
        Organization organization = organizationRepository.save(new Organization("BesterTestnameEver"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        assertThrows(InvalidDataAccessApiUsageException.class, () -> attendanceRepository.save(null));
      //FIXME:  assertThrows(NullPointerException.class, () -> attendanceRepository.save(new AttendanceStatus(null,null,null)));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
    }

    @Test
    public void attendanceBasics() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Hubert der zweite Tester", "testmail@supertestbro.com", "superpasswort2"));
        Organization organization = organizationRepository.save(new Organization("BesterTestnameEver"));
        Calendar calendar = calendarRepository.save(new Calendar("Erstbester Calendar", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erstbester Testname", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        assertEquals(event,attendance.getEvent());
        assertEquals(user,attendance.getUser());
        assertEquals(AttendanceStatusPossibilities.ATTENDING,attendance.getStatus());
    }

}
