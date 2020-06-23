package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.CalendarEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.CommentEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class CommentEndpointTest {

    @Autowired
    CommentEndpoint commentEndpoint;

    @Autowired
    EventEndpoint eventEndpoint;

    @Autowired
    CalendarEndpoint calendarEndpoint;

    @Autowired
    UserEndpoint userEndpoint;

    @Mock
    CommentRepository commentRepository;


    @WithMockUser(username = "User 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void createNewComment_ShouldReturnNewComment() {

        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Event event = new Event("Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar);
        event.setId(1);


        CommentDto commentDto = new CommentDto(null, "test","User 1", 1, LocalDateTime.of(2020, 1, 1, 15, 30));

        CommentDto saved = commentEndpoint.create(commentDto);
        assertEquals(commentDto.getText(), saved.getText());
        assertEquals(commentDto.getUsername(), saved.getUsername());
        assertEquals(commentDto.getEventId(), saved.getEventId());
        assertEquals(commentDto.getUpdateDateTime(), saved.getUpdateDateTime());
    }


}

