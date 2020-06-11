package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;

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
    OrganizationService organizationService;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarService calendarService;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    public void save_thenEdit_shouldReturn_newOrganization() {
        Organization organization = organizationRepository.save(new Organization("Test Organization Service 1"));
        Organization updatedOrganization = new Organization("Updated Name");
        updatedOrganization.setId(organization.getId());
        Organization returnedOrganization = organizationService.update(updatedOrganization);
        assertEquals(updatedOrganization.getName(), returnedOrganization.getName());
        assertEquals(returnedOrganization.getId(), organization.getId());
        organization.setId(2);

    }

    @Test
    public void edit_nonSavedOrganization_shouldThrow_NotFoundException() {
        Integer improbableId = 123456;
        Organization updatedOrganization = new Organization("UpdatedTestName");
        updatedOrganization.setId(improbableId);
        assertThrows(NotFoundException.class, () -> organizationService.update(updatedOrganization));
    }

    @Test
    public void edit_withoutName_shouldThrow_ValidationException() {
        Organization organization = organizationRepository.save(new Organization("Test Organization Service 2"));
        Organization updatedOrganization = new Organization("");
        updatedOrganization.setId(organization.getId());
        assertThrows(ValidationException.class, () -> organizationService.update(updatedOrganization));
    }

    @Test
    public void deleteOrganization_OrganizationShouldBeDeleted() {
        Organization organization = organizationRepository.save(new Organization("Delete Test"));
        organizationService.delete(organization.getId());

        assertThrows(NotFoundException.class, () -> organizationService.findById(organization.getId()));

    }

    @Test
    public void removeCalendarsWhenDeletingOrganization_calendarsShouldNotBeAssociatedWithDeletedOrganization() {
        Organization organization = organizationRepository.save(new Organization("Delete Test"));

        Calendar calendar = new Calendar("Test Cal", new ArrayList<Organization>());
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(calendar);
        organizationService.addCalendars(organization, calendarList);

        assertNotEquals(0, organization.getCalendars().size());

        organizationService.delete(organization.getId());

        //TODO: Revisit this once the connection between Calendar and Organization is properly realized. Not yet testable.

        assertThrows(NotFoundException.class, () -> organizationService.findById(organization.getId()));

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
        organizationService.addCalendars(organization2, Collections.singleton(calendar));

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
        organizationService.removeCalendars(organization2, Collections.singleton(calendar));

        assertIterableEquals(Collections.emptyList(), organizationRepository.findById(organization2.getId()).get().getCalendars());
        assertIterableEquals(List.of(organization1), calendarRepository.findById(calendar.getId()).get().getOrganizations());
    }


    @Test
    public void getOrgMembers(){
        Organization organization = organizationRepository.save(new Organization("Organization Members Test"));
        ApplicationUser member1 =userRepository.save(new ApplicationUser("member1", "member1@org.at", "supersecret"));
        ApplicationUser member2 = userRepository.save(new ApplicationUser("member2", "member2@org.at", "supersecret"));
        Set<OrganizationMembership> organizationMemberships = new HashSet<>();
        organizationMemberships.add(new OrganizationMembership(organization, member1, OrganizationRole.MEMBER));
        organizationMemberships.add(new OrganizationMembership(organization, member2, OrganizationRole.MOD ));
        organization.setMemberships(organizationMemberships);
        organizationRepository.save(organization);

        assertEquals(2, organizationService.getMembers(organization.getId()).size());

    }
}
