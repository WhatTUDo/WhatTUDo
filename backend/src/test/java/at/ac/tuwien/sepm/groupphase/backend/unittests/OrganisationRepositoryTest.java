package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
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
public class OrganisationRepositoryTest {
    @Autowired
    OrganisationRepository organisationRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @Test
    public void repoBasics() {
        //noinspection ConstantConditions
        assertThrows(InvalidDataAccessApiUsageException.class, () -> organisationRepository.save(null));
        assertThrows(NullPointerException.class, () -> organisationRepository.save(new Organisation(null)));
        organisationRepository.save(new Organisation("Test Name"));
        organisationRepository.save(new Organisation("Test Name"));
    }

    @Test
    public void calendarBasics() {
        calendarRepository.save(new Calendar("Calendar", Collections.emptyList())); // TODO: Should Throw

        Organisation orga = organisationRepository.save(new Organisation("Test Name"));
        Calendar cal = calendarRepository.save(new Calendar("Calendar", Collections.singletonList(orga)));
        orga.getCalendars().add(cal);
        assertAll(
            () -> {
                assertEquals(1, cal.getOrganisations().size());
                assertEquals(orga, cal.getOrganisations().get(0));
            },
            () -> {
                assertEquals(1, orga.getCalendars().size());
                assertEquals(cal, orga.getCalendars().get(0));
            }
        );
    }
}
