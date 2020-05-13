package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.OrganisationEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganisationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
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
public class OrganisationEndpointTest {

    @Autowired
    OrganisationEndpoint endpoint;
    @Autowired
    OrganisationRepository organisationRepository;


    @Test
    public void save_thenEdit_shouldReturn_newOrganisation() {
        Organisation organisation = organisationRepository.save(new Organisation("Test Organisation 1"));
        List<Integer> calendars = Collections.emptyList();
        OrganisationDto updatedOrganisation = new OrganisationDto(organisation.getId(), "Updated Test Organisation", calendars);
        OrganisationDto returnedOrganisation = endpoint.editOrganisation(updatedOrganisation);
        assertEquals(returnedOrganisation.getId(),organisation.getId());
        assertEquals(returnedOrganisation.getName(),updatedOrganisation.getName());


    }


    @Test
    public void edit_nonSavedOrganisation_shouldThrow_ResponseStatusException() {
        List<Integer> calendars = Collections.emptyList();
        OrganisationDto organisationDto = new OrganisationDto(15, "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganisation(organisationDto));
    }



    @Test
    public void edit_withoutName_shouldThrow_ResponseStatusException() {
        Organisation organisation = organisationRepository.save(new Organisation("Test Organisation 2"));
        List<Integer> calendars = Collections.emptyList();
        OrganisationDto organisationDto = new OrganisationDto(organisation.getId(), "", calendars);
        assertThrows(ResponseStatusException.class, () -> endpoint.editOrganisation(organisationDto));

    }

}

