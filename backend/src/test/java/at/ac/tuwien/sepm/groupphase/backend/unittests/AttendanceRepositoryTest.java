package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext
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

    @Test
    public void repoBasics() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Herbert Gutmann", "testmail@supertest.com", "superpasswort"));
        Organization organization = organizationRepository.save(new Organization("Lesezirkel Algebra"));
        Calendar calendar = calendarRepository.save(new Calendar("LeseEvents", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Große Lesenacht", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        assertThrows(InvalidDataAccessApiUsageException.class, () -> attendanceRepository.save(null));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
    }

    @Test
    public void attendanceBasics() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Martina Bestfrau", "testmail@supertestbro.com", "1236541"));
        Organization organization = organizationRepository.save(new Organization("GewichtheberInnen TU Wien"));
        Calendar calendar = calendarRepository.save(new Calendar("Diet", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Massephase", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        assertEquals(event, attendance.getEvent());
        assertEquals(user, attendance.getUser());
        assertEquals(AttendanceStatusPossibilities.ATTENDING, attendance.getStatus());
    }

}
