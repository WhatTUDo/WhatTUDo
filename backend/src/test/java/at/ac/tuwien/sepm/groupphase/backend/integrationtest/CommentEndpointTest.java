package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.CommentEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommentMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
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
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class CommentEndpointTest {


    @Autowired
    CommentEndpoint commentEndpoint;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;


    @Autowired
    UserEndpoint userEndpoint;


    @Autowired
    CommentMapper commentMapper;

    @Mock
    CalendarRepository calendarRepository;


    @Mock
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    @WithMockUser(username = "User 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_shouldReturn_sameComment() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Location location = locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));


        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId(), location.getId());

        eventDto.setId(1);

        IncomingUserDto userDto = new IncomingUserDto(0, "Test", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        CommentDto commentDto = new CommentDto(null, "Test Text", "Test", 1, LocalDateTime.of(2020, 1, 1, 15, 30));

        CommentDto returnedComment = commentEndpoint.create(commentDto);
        assertNotNull(returnedComment);
        assertEquals(commentDto.getText(), returnedComment.getText());
        assertEquals(commentDto.getUsername(), returnedComment.getUsername());
        assertEquals(commentDto.getEventId(), returnedComment.getEventId());
        //Note: Update Time can't be the same, that is okay.
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_thenFindById_shouldReturn_SameComment() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Location location = locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));


        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId(), location.getId());

        eventDto.setId(1);

        IncomingUserDto userDto = new IncomingUserDto(0, "TestID", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        CommentDto commentDto = new CommentDto(null, "Test Text for get ID", "TestID", 1, LocalDateTime.of(2020, 1, 1, 15, 30));

        CommentDto returnedComment = commentEndpoint.create(commentDto);

        CommentDto findById = commentEndpoint.getById(returnedComment.getId());

        assertNotNull(returnedComment);
        assertEquals(findById.getText(), returnedComment.getText());
        assertEquals(findById.getUsername(), returnedComment.getUsername());
        assertEquals(findById.getEventId(), returnedComment.getEventId());
    }

    @WithMockUser(username = "Person 1", authorities = {"SYSADMIN"})
    @Test
    @Transactional
    public void savedComment_then_getCommentsByUserId_ReturnsComment() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Location location = locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));


        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId(), location.getId());

        eventDto.setId(1);

        IncomingUserDto userDto = new IncomingUserDto(0, "TestByUserId", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        CommentDto commentDto = new CommentDto(null, "Test Text for findbyUserId Comment", "TestByUserId", 1, LocalDateTime.of(2020, 1, 1, 15, 30));
        CommentDto returnedComment = commentEndpoint.create(commentDto);


        for (CommentDto c : commentEndpoint.getCommentsByUserId(savedUserDto.getId())
             ) {

            assertNotNull(returnedComment);
            assertEquals(c.getText(), returnedComment.getText());
            assertEquals(c.getUsername(), returnedComment.getUsername());
            assertEquals(c.getEventId(), returnedComment.getEventId());

        }
    }

    @WithMockUser(username = "Person 1", authorities = {"SYSADMIN"})
    @Test
    @Transactional
    public void savedTwoComments_then_getAllComments_ReturnsPreviousSizePlusTwo() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Location location = locationRepository.save(new Location("Test Location", "Test Adress", "Zip", 0, 0));



        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId(), location.getId());

        eventDto.setId(1);

        IncomingUserDto userDto = new IncomingUserDto(0, "TestFindAll", "testy@test.com", "hunter2");
        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        Collection<CommentDto> returnedComments = commentEndpoint.getAllComments();


        CommentDto commentDto = new CommentDto(null, "Test Text for findAll Comments", "TestFindAll", 1, LocalDateTime.of(2020, 1, 1, 15, 30));
        CommentDto returnedComment = commentEndpoint.create(commentDto);
        CommentDto commentDto2 = new CommentDto(null, "Test Text for findAll Comments2", "TestFindAll", 1, LocalDateTime.of(2020, 1, 1, 15, 30));
        CommentDto returnedComment2 = commentEndpoint.create(commentDto2);

        returnedComments.add(returnedComment);
        returnedComments.add(returnedComment2);

        assertEquals(commentEndpoint.getAllComments().size(), returnedComments.size());
    }


}

