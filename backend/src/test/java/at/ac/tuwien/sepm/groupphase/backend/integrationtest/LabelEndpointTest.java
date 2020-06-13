package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.LabelEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class LabelEndpointTest {

    @Autowired
    LabelEndpoint labelEndpoint;

    @WithMockUser(username = "Person 1")
    @Test
    public void createNewLabel_ShouldReturnNewLabel() {
        LabelDto labelDto = new LabelDto(0, "TestLabel", new ArrayList<>());
        LabelDto saved = labelEndpoint.create(labelDto);
        assertEquals(labelDto.getName(), saved.getName());
    }


}
