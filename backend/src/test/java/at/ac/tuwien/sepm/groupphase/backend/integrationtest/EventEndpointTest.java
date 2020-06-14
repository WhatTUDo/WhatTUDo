package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LabelMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleCalendarService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class EventEndpointTest {
//
//    @Autowired
//    MockMvc mockMvc;

    @Autowired
    EventEndpoint endpoint;

    @Mock
     CalendarRepository calendarRepository;

    @Mock
     OrganizationRepository organizationRepository;

    @InjectMocks
    SimpleCalendarService calendarService;

    @InjectMocks
    SimpleCalendarService organizationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    EventMapper eventMapper;


//    @Autowired
//     JwtTokenizer jwtTokenizer;
//
//    @Autowired
//     SecurityProperties securityProperties;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders
//            .webAppContextSetup(context)
//            .apply(springSecurity()) // enable security for the mock set up
//            .build();
//    }


    @WithMockUser(username = "User 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_shouldReturn_sameEvent() throws Exception {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));

        EventDto eventDto = new EventDto(1, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());

         EventDto returnedEvent = endpoint.post(eventDto);
        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_thenRead_shouldReturn_sameEvent() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
//        Organization orga = organizationRepository.save(new Organization("Test Organization2"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar2", Collections.singletonList(orga)));
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

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_withoutCorrectParam_shouldReturn_ResponseStatusException() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
//        Organization orga = organizationRepository.save(new Organization("Test Organization3"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar3", Collections.singletonList(orga)));
        EventDto eventDto1 = new EventDto(null, "", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto eventDto2 = new EventDto(null, "Test Event", LocalDateTime.of(2020, 1, 2, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0),calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto2));
    }

//    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
//    @Test
//    public void save_withNoArgs_shouldReturn_ResponseStatusException() {
//        EventDto eventDto = new EventDto();
//        assertThrows(ResponseStatusException.class, () -> endpoint.post(eventDto));
//    }

//    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
//    @Test
//    public void save_nullObject_shouldReturn_ResponseStatusException() {
//        assertThrows(ResponseStatusException.class, () -> endpoint.post(null));
//    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void get_validID_shouldReturn_EventWithSpecifiedID() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
//
//        Organization orga = organizationRepository.save(new Organization("Test Organization9"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar9", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(6, "Test Name_new", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());

        EventDto returnedEvent = endpoint.post(eventDto);
        assertNotNull(returnedEvent);
        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());
    }

//    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
//    @Test
//    public void get_invalidID_shouldReturn_ResponseStatusException_With404Code() {
//        int uselessID = 123456;
//        assertThrows(ResponseStatusException.class, () -> endpoint.getById(uselessID));
//        try {
//            endpoint.getById(uselessID);
//        } catch (ResponseStatusException e) {
//            assertEquals(404, e.getStatus().value());
//        }
//    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void get_multipleEvents_WithValidStartEndDates_shouldReturn_listOfEventDtos() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
//        Organization orga = organizationRepository.save(new Organization("Test Organization10"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar10", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(10, "Test Name10", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0),calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 8, 0, 0);

        List<EventDto> eventDtos = endpoint.getMultiple(start, end);

        assertNotEquals(0, eventDtos.size());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsResponseStatusException() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto notSavedEvent = new EventDto(null, "Non Existent", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.deleteEvent(notSavedEvent));
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void delete_savedEvent_findBYIdReturnsResponseException() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto eventDto = new EventDto(null, "Delete Event", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0),calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);
        endpoint.deleteEvent(returnedEvent);
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(returnedEvent.getId()));
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void updateEntityValues_shouldReturn_correctChanges() {
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
//        Organization orga = organizationRepository.save(new Organization("Test Organization6"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar6", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(3, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0),calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0),1);

        EventDto finalEvent = endpoint.editEvent(eventDtoChanges);

        System.out.println(returnedEvent.getId());
        System.out.println(eventDtoChanges.getId());

        assertEquals(finalEvent.getName(), eventDtoChanges.getName());
        assertEquals(finalEvent.getEndDateTime(), eventDtoChanges.getEndDateTime());
        assertEquals(finalEvent.getStartDateTime(), eventDtoChanges.getStartDateTime());
        assertEquals(finalEvent.getCalendarId(), eventDtoChanges.getCalendarId());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void updateEntityStartDateBefore2020_throwsResponseException() {
//        Organization orga = organizationRepository.save(new Organization("Test Organization7"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar7", Collections.singletonList(orga)));
        Organization orga;
        Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto eventDto = new EventDto(4, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2000, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), 1);

        assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void updateEntityStartDateBeforeEndDate_throwsResponseException() {
        Organization orga;
     Calendar calendar;
        orga = new Organization("Test Organization2");
        orga.setId(1);
        calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));

//        Organization orga = organizationRepository.save(new Organization("Test Organization8"));
//        Calendar calendar = calendarRepository.save(new Calendar("Test Calendar8", Collections.singletonList(orga)));
        EventDto eventDto = new EventDto(5, "Test Name", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId());
        EventDto returnedEvent = endpoint.post(eventDto);

        assertEquals(eventDto.getName(), returnedEvent.getName());
        assertEquals(eventDto.getEndDateTime(), returnedEvent.getEndDateTime());
        assertEquals(eventDto.getStartDateTime(), returnedEvent.getStartDateTime());
        assertEquals(eventDto.getCalendarId(), returnedEvent.getCalendarId());

        EventDto eventDtoChanges = new EventDto(returnedEvent.getId(), "Test2", LocalDateTime.of(2022, 1, 1, 17, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar.getId() );

       assertThrows(ResponseStatusException.class, () -> endpoint.editEvent(eventDtoChanges));

    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void updateLabelsOfEvent_returnsEventWithNewLabels_RemoveLabel_findLabelsByIdIsEmpty(){
        Organization orga = new Organization("Test Organization2");
        orga.setId(1);
        Calendar calendar = new Calendar("Test Calendar2", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization2"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto eventDto =  endpoint.post(new EventDto(null, "Update Label", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId()));
        Label label = labelRepository.save(new Label("Label 1"));
        LabelDto labelDto = labelMapper.labelToLabelDto(label);
        eventDto = endpoint.updateLabelsOfEvent(eventDto.getId(), Collections.singletonList(labelDto));
        Optional<Label> label1 = labelRepository.findByName(label.getName());

        assertEquals( eventDto.getName(),label1.get().getEvents().get(0).getName());

        endpoint.removeLabelsFromEvent(eventDto.getId(), Collections.singletonList(label.getId()));
        assertTrue(labelRepository.findById(label.getId()).get().getEvents().isEmpty());

        EventDto finalEventDto = eventDto;
        assertTrue(endpoint.getLabelsById(finalEventDto.getId()).isEmpty());

    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    @Transactional
    public void updateLabelsOfEvent_findLabelsById_returnsLabel() {
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Calendar calendar = new Calendar("Test Calendar", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto eventDto =  endpoint.post(new EventDto(null, "Find Label", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId()));
        Label label = labelRepository.save(new Label("Test"));
        LabelDto labelDto = labelMapper.labelToLabelDto(label);
        eventDto = endpoint.updateLabelsOfEvent(eventDto.getId(), Collections.singletonList(labelDto));

        assertEquals(label.getName(), endpoint.getLabelsById(eventDto.getId()).get(0).getName());
    }

    @WithMockUser(username = "Person 1", authorities ={"MOD_1", "MEMBER_1"})
    @Test
    public void getEventByCalendarId_returnsEvent(){
        Organization orga = new Organization("Test Organization");
        orga.setId(1);
        Calendar calendar = new Calendar("Test Calendar", Collections.singletonList(orga));
        calendar.setId(1);
        Mockito.when(organizationRepository.save( new Organization("Test Organization"))).thenReturn(orga);
        Mockito.when(calendarRepository.save(calendar)).thenReturn(calendar);
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.ofNullable(orga));
        Mockito.when(calendarRepository.findById(1)).thenReturn(Optional.ofNullable(calendar));
        EventDto eventDto =  endpoint.post(new EventDto(null, "Find Event", LocalDateTime.of(2020, 1, 1, 15, 30), LocalDateTime.of(2020, 1, 1, 16, 0), calendar.getId()));

        List<EventDto> found = endpoint.getEventsByCalendarId(1);
        boolean b = false;
        for (EventDto e :found){
            if(e.getId().equals(eventDto.getId())){
                b = true;
                break;
            }
        }
        if(!b){
            fail();
        }
    }

}

