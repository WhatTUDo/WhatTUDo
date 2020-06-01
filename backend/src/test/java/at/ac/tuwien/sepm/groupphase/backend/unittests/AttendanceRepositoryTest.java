package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class AttendanceRepositoryTest {
    @Autowired
    AttendanceRepository attendanceRepository;

    @Test
    public void repoBasics() {
        //noinspection ConstantConditions
        assertThrows(InvalidDataAccessApiUsageException.class, () -> attendanceRepository.save(null));
        assertThrows(NullPointerException.class, () -> attendanceRepository.save(new AttendanceStatus(null,null,null)));
      //  attendanceRepository.save(new AttendanceStatus("Test Name"));
    }
/*
    @Test
    public void organizationBasics() {
        Organization orga = organizationRepository.save(new Organization("Test Organization 1"));
        Calendar cal = calendarRepository.save(new Calendar("Calendar", Collections.singletonList(orga)));
        orga.getCalendars().add(cal);
        assertAll(
            () -> {
                assertEquals(1, cal.getOrganizations().size());
                assertEquals(orga, cal.getOrganizations().get(0));
            },
            () -> {
                assertEquals(1, orga.getCalendars().size());
                assertEquals(cal, orga.getCalendars().get(0));
            }
        );
    }
    */
}
