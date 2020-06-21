package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.OrganizationEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.aspectj.weaver.ast.Not;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class OrganizationEndpointTest {

    @Autowired
    OrganizationEndpoint endpoint;

    @Autowired
    UserService userService;

    @WithMockUser(username = "Person 1", roles = {"SYSADMIN"})
    @Test
    public void createNewOrganisation_shouldReturnNewOrganization() {
        OrganizationDto organizationDto = new OrganizationDto(0, "test", new ArrayList<>());

        OrganizationDto createdDto = endpoint.createOrganization(organizationDto);

        assertEquals(organizationDto.getName(), createdDto.getName());
    }

    @WithMockUser(username = "Person 1", roles = {"SYSADMIN"})
    @Test
    public void createNewOrganisation_withDescription_shouldReturnNewOrganization() {
        OrganizationDto organizationDto = new OrganizationDto(0, "test with Description", new ArrayList<>(), "Description");

        OrganizationDto createdDto = endpoint.createOrganization(organizationDto);

        assertEquals(organizationDto.getName(), createdDto.getName());
        assertEquals(organizationDto.getDescription(), createdDto.getDescription());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getAllOrganisations_shouldReturnListOfOrganizations() {
        Organization organization = (new Organization("Test Organization"));
        organization.setId(1);

        List<OrganizationDto> organizationDtos = endpoint.getAllOrgas();
        assertNotEquals(0, organizationDtos.size());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getSingleOrganization_shouldReturnThisOrganization() {
        Integer id = 1;
        Organization organization = (new Organization("Test Organization"));
        organization.setId(id);

        OrganizationDto organizationDto = endpoint.getOrgaById(id);

        assertEquals(organization.getId(), organizationDto.getId());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getSingleOrganizaion_ifNonExistantIdIsSupplied_NotFoundHttpCodeIsReturned() {
        Integer id = 1;
        Integer nonsenseID = 123456;
        Organization organization = (new Organization("Test Organization"));
        organization.setId(id);

        assertThrows(ResponseStatusException.class, () -> endpoint.getOrgaById(nonsenseID));

        try {
            endpoint.getOrgaById(nonsenseID);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }


    @WithMockUser(username = "Person 1",   authorities = {"ADMIN_1","MOD_1", "MEMBER_1"})
    @Test
    public void save_thenEdit_shouldReturn_newOrganization() {
        Organization organization = (new Organization("Test Organization 1"));
        organization.setId(1);
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto updatedOrganization = new OrganizationDto(organization.getId(), "Updated Test Organization", calendars);
        OrganizationDto returnedOrganization = endpoint.editOrganization(updatedOrganization);
        assertEquals(returnedOrganization.getId(), organization.getId());
        assertEquals(returnedOrganization.getName(), updatedOrganization.getName());
    }


    @WithMockUser(username = "Person 1",   authorities = {"ADMIN_1","MOD_1", "MEMBER_1"})
    @Test
    public void edit_withoutName_shouldThrow_ResponseStatusException() {
        Organization organization = (new Organization("Test Organization 1"));
        organization.setId(1);
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto organizationDto = new OrganizationDto(organization.getId(), "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganization(organizationDto));
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_0", "MEMBER_0"})
    @Test
    public void deleteOrganizationThatDoesNotExist_throwResponseStatusException() {
        assertThrows(ResponseStatusException.class, () -> endpoint.getOrgaById(0));
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_0", "MEMBER_0"})
    @Test
    public void addCalendarsToOrgWithUnknownId_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> endpoint.addCalToOrga(0, Collections.singletonList(0)));
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_0", "MEMBER_0"})
    @Test
    public void removeCalendarsOfUnknownOrganization_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> endpoint.removeCalFromOrga(0, Collections.singletonList(0)));
    }

    @WithMockUser(username = "Person 1", roles = {"SYSADMIN"})
    @Test
    public void getOrganizationMembers() {
        OrganizationDto organizationDto = new OrganizationDto(0, "get members", new ArrayList<>());

        OrganizationDto createdDto = endpoint.createOrganization(organizationDto);

        assertEquals(organizationDto.getName(), createdDto.getName());


        assert (endpoint.getOrganizationMembers(createdDto.getId()).isEmpty());

    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_0", "MEMBER_0"})
    @Test
    public void addMemberToNonExistentOrg_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> endpoint.addMembership(0, 0, "MOD"));
    }


}
