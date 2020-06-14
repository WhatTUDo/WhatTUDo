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
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
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
    @Mock
    OrganizationRepository organizationRepository;

    @WithMockUser(username = "Person 1", roles = {"SYSADMIN"})
    @Test
    public void createNewOrganisation_shouldReturnNewOrganization() {
        OrganizationDto organizationDto = new OrganizationDto(0, "test", new ArrayList<>());

        OrganizationDto createdDto = endpoint.createOrganization(organizationDto);

        assertEquals(organizationDto.getName(), createdDto.getName());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getAllOrganisations_shouldReturnListOfOrganizations() {
        Organization organization = (new Organization("Test Organization"));
        organization.setId(1);
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

        List<OrganizationDto> organizationDtos = endpoint.getAllOrgas();
        assertNotEquals(0, organizationDtos.size());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getSingleOrganization_shouldReturnThisOrganization() {
        Integer id = 1;
        Organization organization = (new Organization("Test Organization"));
        organization.setId(id);
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

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
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

        assertThrows(ResponseStatusException.class, () -> endpoint.getOrgaById(nonsenseID));

        try {
            endpoint.getOrgaById(nonsenseID);
        }
        catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }


    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_thenEdit_shouldReturn_newOrganization() {
        Organization organization =(new Organization("Test Organization 1"));
        organization.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization 1"))).thenReturn(organization);
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto updatedOrganization = new OrganizationDto(organization.getId(), "Updated Test Organization", calendars);
        OrganizationDto returnedOrganization = endpoint.editOrganization(updatedOrganization);
        assertEquals(returnedOrganization.getId(),organization.getId());
        assertEquals(returnedOrganization.getName(),updatedOrganization.getName());
    }

//    @WithMockUser(username = "Person 1", authorities = {"MOD_200000", "MEMBER_200000"})
//    @Test
//    public void edit_nonSavedOrganization_shouldThrow_ResponseStatusException() {
//        List<Integer> calendars = Collections.emptyList();
//        OrganizationDto organizationDto = new OrganizationDto(200000, "newFalseName", calendars);
//        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganization(organizationDto));
//    }
    //This test will throw NotFoundException, because when we check the authorities, the Org Id will not be found and it wont allow user to go any further.


    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void edit_withoutName_shouldThrow_ResponseStatusException() {
        Organization organization =(new Organization("Test Organization 1"));
        organization.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization 1"))).thenReturn(organization);
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto organizationDto = new OrganizationDto(organization.getId(), "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganization(organizationDto));
    }

}

