package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrganisationServiceTest {

    @Autowired
    OrganisationService service;
    @Autowired
    OrganisationRepository organisationRepository;
    @Autowired
    CalendarService calendarService;
    @Autowired
    CalendarRepository calendarRepository;


    @Test
    public void save_thenEdit_shouldReturn_newOrganisation() {
        Organisation organisation = organisationRepository.save(new Organisation("Test Organisation Service 1"));
        Organisation updatedOrganisation = new Organisation("Updated Name");
        updatedOrganisation.setId(organisation.getId());
        Organisation returnedOrganisation = service.update(updatedOrganisation);
        assertEquals(updatedOrganisation.getName(), returnedOrganisation.getName());
        assertEquals(returnedOrganisation.getId(), organisation.getId());
        organisation.setId(2);

    }

    @Test
    public void edit_nonSavedOrganisation_shouldThrow_NotFoundException() {
        Organisation updatedOrganisation = new Organisation("UpdatedTestName");
        updatedOrganisation.setId(3);
        assertThrows(NotFoundException.class, () -> service.update(updatedOrganisation));
    }

    @Test
    public void edit_withoutName_shouldThrow_ValidationException() {
        Organisation organisation = organisationRepository.save(new Organisation("Test Organisation Service 2"));
        Organisation updatedOrganisation = new Organisation("");
        updatedOrganisation.setId(organisation.getId());
        assertThrows(ValidationException.class, () -> service.update(updatedOrganisation));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Transactional
    public void addCalendarToOrga() {
        // Prep
        Organisation organisation1 = organisationRepository.save(new Organisation("Orga 1"));
        Calendar calendar = calendarRepository.save(new Calendar("Test", Lists.newArrayList(organisation1)));
        organisation1.getCalendars().add(calendar);
        organisationRepository.save(organisation1);

        // Test
        Organisation organisation2 = organisationRepository.save(new Organisation("Orga 2"));
        service.addCalendars(organisation2, Collections.singleton(calendar));

        assertIterableEquals(Collections.singleton(calendar), organisationRepository.findById(organisation2.getId()).get().getCalendars());
        assertIterableEquals(List.of(organisation1, organisation2), calendarRepository.findById(calendar.getId()).get().getOrganisations());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Transactional
    public void removeCalendarFromOrga() {
        // Prep
        Organisation organisation1 = organisationRepository.save(new Organisation("Orga 1"));
        Organisation organisation2 = organisationRepository.save(new Organisation("Orga 2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test", Lists.newArrayList(organisation1)));
        organisation1.getCalendars().add(calendar);
        organisation2.getCalendars().add(calendar);
        organisationRepository.saveAll(List.of(organisation1, organisation2));

        // Test
        service.removeCalendars(organisation2, Collections.singleton(calendar));

        assertIterableEquals(Collections.emptyList(), organisationRepository.findById(organisation2.getId()).get().getCalendars());
        assertIterableEquals(List.of(organisation1), calendarRepository.findById(calendar.getId()).get().getOrganisations());
    }
}
