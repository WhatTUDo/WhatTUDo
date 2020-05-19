package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
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
public class OrganizationServiceTest {

    @Autowired
    OrganizationService service;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarService calendarService;
    @Autowired
    CalendarRepository calendarRepository;


    @Test
    public void save_thenEdit_shouldReturn_newOrganization() {
        Organization organization = organizationRepository.save(new Organization("Test Organization Service 1"));
        Organization updatedOrganization = new Organization("Updated Name");
        updatedOrganization.setId(organization.getId());
        Organization returnedOrganization = service.update(updatedOrganization);
        assertEquals(updatedOrganization.getName(), returnedOrganization.getName());
        assertEquals(returnedOrganization.getId(), organization.getId());
        organization.setId(2);

    }

    @Test
    public void edit_nonSavedOrganization_shouldThrow_NotFoundException() {
        Organization updatedOrganization = new Organization("UpdatedTestName");
        updatedOrganization.setId(3);
        assertThrows(NotFoundException.class, () -> service.update(updatedOrganization));
    }

    @Test
    public void edit_withoutName_shouldThrow_ValidationException() {
        Organization organization = organizationRepository.save(new Organization("Test Organization Service 2"));
        Organization updatedOrganization = new Organization("");
        updatedOrganization.setId(organization.getId());
        assertThrows(ValidationException.class, () -> service.update(updatedOrganization));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Transactional
    public void addCalendarToOrga() {
        // Prep
        Organization organization1 = organizationRepository.save(new Organization("Orga 1"));
        Calendar calendar = calendarRepository.save(new Calendar("Test", Lists.newArrayList(organization1)));
        organization1.getCalendars().add(calendar);
        organizationRepository.save(organization1);

        // Test
        Organization organization2 = organizationRepository.save(new Organization("Orga 2"));
        service.addCalendars(organization2, Collections.singleton(calendar));

        assertIterableEquals(Collections.singleton(calendar), organizationRepository.findById(organization2.getId()).get().getCalendars());
        assertIterableEquals(List.of(organization1, organization2), calendarRepository.findById(calendar.getId()).get().getOrganizations());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @Transactional
    public void removeCalendarFromOrga() {
        // Prep
        Organization organization1 = organizationRepository.save(new Organization("Orga 1"));
        Organization organization2 = organizationRepository.save(new Organization("Orga 2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test", Lists.newArrayList(organization1)));
        organization1.getCalendars().add(calendar);
        organization2.getCalendars().add(calendar);
        organizationRepository.saveAll(List.of(organization1, organization2));

        // Test
        service.removeCalendars(organization2, Collections.singleton(calendar));

        assertIterableEquals(Collections.emptyList(), organizationRepository.findById(organization2.getId()).get().getCalendars());
        assertIterableEquals(List.of(organization1), calendarRepository.findById(calendar.getId()).get().getOrganizations());
    }
}
