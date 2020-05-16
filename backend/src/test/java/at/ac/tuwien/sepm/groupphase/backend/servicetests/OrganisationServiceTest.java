package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrganisationServiceTest {

    @Autowired
    OrganisationService service;
    @Autowired
    OrganisationRepository organisationRepository;


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


}
