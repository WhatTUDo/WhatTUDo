package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.LabelEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class LabelEndpointTest {

    @Autowired
    LabelEndpoint labelEndpoint;

    @Autowired
    EventEndpoint eventEndpoint;

    @Mock
    LabelRepository labelRepository;

    @WithMockUser(roles = {"SYSADMIN"})
    @Test
    public void createNewLabel_ShouldReturnNewLabel() {
        LabelDto labelDto = new LabelDto(0, "TestLabel", new ArrayList<>());
        LabelDto saved = labelEndpoint.create(labelDto);
        assertEquals(labelDto.getName(), saved.getName());
    }

    @WithMockUser(username = "Person 1", roles = {"SYSADMIN"})
    @Test
    public void deleteLabel_labelShouldBeDeleted() {
        LabelDto labelDto = new LabelDto(0, "TestLabel", new ArrayList<>());
        LabelDto saved = labelEndpoint.create(labelDto);

        labelEndpoint.delete(saved.getId());
        assertThrows(ResponseStatusException.class, () -> labelEndpoint.getById(saved.getId()));

        try {
            labelEndpoint.getById(saved.getId());
        }
        catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @WithMockUser(username = "Person 1")
    @Test
    public void getAllLabels_ListOfAllLabelsShouldBeReturned() {
        Label label = new Label("TestLabel");

        Mockito.when(labelRepository.save(label)).thenReturn(label);

        labelRepository.save(label);

        List<LabelDto> labelDtos = labelEndpoint.getAllLabels();
        assertNotEquals(0, labelDtos.size());
    }

    @WithMockUser(roles = {"SYSADMIN"})
    @Test
    public void updateLabel_LabelShouldBeUpdated() {
        LabelDto labelDto = labelEndpoint.create(new LabelDto(0, "TestLabel", new ArrayList<>()));
        assertDoesNotThrow(() -> labelEndpoint.getById(labelDto.getId()));

        LabelDto updated = new LabelDto(labelDto.getId(), "TestLabelUpdate", labelDto.getEventIds());
        labelEndpoint.update(updated);

        assertNotEquals(labelDto.getName(), updated.getName());
        assertEquals(labelDto.getId(), updated.getId());
    }


}
