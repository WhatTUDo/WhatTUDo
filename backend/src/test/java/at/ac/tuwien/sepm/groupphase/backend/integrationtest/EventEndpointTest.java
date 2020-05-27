package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.auth.jwt.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@AutoConfigureMockMvc
public class EventEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventEndpoint endpoint;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
     JwtTokenizer jwtTokenizer;

    @Autowired
     SecurityProperties securityProperties;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity()) // enable security for the mock set up
            .build();
    }




    @Test
    public void save_shouldReturn_sameEvent() throws Exception {

//       this.organizationRepository = mock(OrganizationRepository.class);
//        this.calendarRepository = mock(CalendarRepository.class);
//
//        Organization organization = new Organization("Test Organization1");
//       organization.setId(1);
//       when(this.organizationRepository.save(new Organization("Test Organization1"))).thenReturn(organization);
//        Calendar calendar = new Calendar("Test Calendar1", Collections.singletonList(organization));
//        calendar.setId(1);
//        when(this.calendarRepository.save(new Calendar("Test Calendar1", Collections.singletonList(organization)))).thenReturn(calendar);


        Organization orga = organizationRepository.save(new Organization("Test Organization1"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar1", Collections.singletonList(orga)));
        ApplicationUser user = new ApplicationUser("admin", "admin@org.com", "hunter");
        Set<OrganizationMembership> memberships = new HashSet<>();
        memberships.add(new OrganizationMembership(orga, user, OrganizationRole.MEMBER));
        user.setMemberships(memberships);
        user = userRepository.save(user);

        //EventDto eventDto = new EventDto(1, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        String jsonString = "{ \"id\":1,\"name\":\"Test Name\", \"startDateTime\": \"2020-01-01T15:30:00\", \"endDateTime\": \"2020-01-01T15:30:00\", \"calendarId\":"+calendar.getId()+"}";
            MvcResult mvcResult = this.mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON).content(jsonString)
              .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getName(),user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))))
                .andDo(print())
                .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            assertEquals(HttpStatus.OK.value(), response.getStatus());


      // EventDto returnedEvent = endpoint.post(eventDto);
//        assertEquals(eventDto.getName(), returnedEvent.getName());
//        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
//        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
//        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

    @Test
    public void save_thenRead_shouldReturn_sameEvent() {
        Organization orga = organizationRepository.save(new Organization("Test Organization2"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar2", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(null, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        EventDto gottenEvent = endpoint.getById(returnedEvent.getId());
        Optional<Calendar> fetchCalendar = calendarRepository.findById(returnedEvent.getCalendarId());
        Calendar returnedCalendar = fetchCalendar.get();
        fetchCalendar = calendarRepository.findById(gottenEvent.getCalendarId());
        Calendar gottenCalendar = fetchCalendar.get();

        assertEquals(gottenEvent.getName(), returnedEvent.getName());
        assertEquals(gottenEvent.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(gottenEvent.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(gottenEvent.getId(), returnedEvent.getId());
        assertEquals(returnedCalendar.getName(), gottenCalendar.getName());
        assertEquals(returnedCalendar.getId(), gottenCalendar.getId());

    }

    @Test
    public void save_withoutCorrectParam_shouldReturn_ResponseStatusException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization3"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar3", Collections.singletonList(orga)));
        EventDto eventDto1 = new EventDto(null, "", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto eventDto2 = new EventDto(null, "Test Event", LocalDateTime.of(2020, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto2));
    }

    @Test
    public void save_withNoArgs_shouldReturn_ResponseStatusException() {
        EventDto eventDto = new EventDto();
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto));
    }

    @Test
    public void save_nullObject_shouldReturn_ResponseStatusException() {
        assertThrows(ResponseStatusException.class, () -> endpoint.post(null));
    }

    @Test
    public void get_validID_shouldReturn_EventWithSpecifiedID() {


        Organization orga = organizationRepository.save(new Organization("Test Organization9"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar9", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(6, "Test Name_new", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());

        EventDto returnedEvent = endpoint.post(eventDto);
        assertNotNull(returnedEvent);
        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

    @Test
    public void get_invalidID_shouldReturn_ResponseStatusException_With404Code() {
        int uselessID = 123456;
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(uselessID));
        try {
            endpoint.getById(uselessID);
        } catch (ResponseStatusException e) {
            assertEquals(404, e.getStatus().value());
        }
    }

    @Test
    public void get_multipleEvents_WithValidStartEndDates_shouldReturn_listOfEventDtos() {
        Organization orga = organizationRepository.save(new Organization("Test Organization10"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar10", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(10, "Test Name10", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 8, 0, 0);

        List<EventDto> eventDtos = endpoint.getMultiple(start, end);

        assertNotEquals(0, eventDtos.size());
    }

    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsResponseStatusException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization4"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar4", Collections.singletonList(orga)));
        EventDto notSavedEvent = new EventDto(null, "Non Existent", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.deleteEvent(notSavedEvent));
    }


    @Test
    public void delete_savedEvent_findBYIdReturnsResponseException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization5"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar5", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(null, "Delete Event", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        endpoint.deleteEvent(returnedEvent);
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(returnedEvent.getId()));
    }


    @Test
    public void updateEntityValues_shouldReturn_correctChanges() {
        Organization orga = organizationRepository.save(new Organization("Test Organization6"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar6", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(3, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        EventDto finalEvent = endpoint.editEvent(eventDtoChanges);

        System.out.println(returnedEvent.getId());
        System.out.println(eventDtoChanges.getId());

        assertEquals(finalEvent.getName(), eventDtoChanges.getName());
        assertEquals(finalEvent.getEndDateTime(), eventDtoChanges.getEndDateTime());
        assertEquals(finalEvent.getStartDateTime(), eventDtoChanges.getStartDateTime());
        assertEquals(finalEvent.getCalendarId(), eventDtoChanges.getCalendarId());
    }

    @Test
    public void updateEntityStartDateBefore2020_throwsResponseException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization7"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar7", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(4, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2000, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

    @Test
    public void updateEntityStartDateBeforeEndDate_throwsResponseException() {
        Organization orga = organizationRepository.save(new Organization("Test Organization8"));
        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar8", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(5, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2022, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId());

        assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

}

