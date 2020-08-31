package at.whattudo.unittests;

import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext
public class OrganizationRepositoryTest {
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @Test
    public void repoBasics() {
        //noinspection ConstantConditions
        assertThrows(InvalidDataAccessApiUsageException.class, () -> organizationRepository.save(null));
        assertThrows(NullPointerException.class, () -> organizationRepository.save(new Organization(null)));
        organizationRepository.save(new Organization("Test Name"));
    }

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
}
