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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrganizationEndpointTest {

    @Autowired
    OrganizationEndpoint endpoint;
    @Autowired
    OrganizationRepository organizationRepository;


    @Test
    public void save_thenEdit_shouldReturn_newOrganization() {
        Organization organization = organizationRepository.save(new Organization("Test Organization 1"));
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto updatedOrganization = new OrganizationDto(organization.getId(), "Updated Test Organization", calendars);
        OrganizationDto returnedOrganization = endpoint.editOrganization(updatedOrganization);
        assertEquals(returnedOrganization.getId(),organization.getId());
        assertEquals(returnedOrganization.getName(),updatedOrganization.getName());


    }


    @Test
    public void edit_nonSavedOrganization_shouldThrow_ResponseStatusException() {
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto organizationDto = new OrganizationDto(15, "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganization(organizationDto));
    }



    @Test
    public void edit_withoutName_shouldThrow_ResponseStatusException() {
        Organization organization = organizationRepository.save(new Organization("Test Organization 2"));
        List<Integer> calendars = Collections.emptyList();
        OrganizationDto organizationDto = new OrganizationDto(organization.getId(), "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganization(organizationDto));

    }

}

