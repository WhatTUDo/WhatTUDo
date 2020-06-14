package at.ac.tuwien.sepm.groupphase.backend.servicetests;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.StatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class AttendanceServiceTest {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    StatusMapper mapper;

    Organization organization;

    @Before
    public void createData() {
        this.organization = organizationRepository.save(new Organization("BesterTestnameEver"));
    }

    @Test
    public void create_shouldReturn_AttendanceStatus() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Dorian", "grazie@gmx.com", "pwdsuperstrong"));
        Calendar calendar = calendarRepository.save(new Calendar("Katzenkalenderreleases", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Adventskatzenkalender", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendance = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus returnedAttendance = attendanceService.create(attendance);
        assertEquals(attendance.getUser(), returnedAttendance.getUser());
        assertEquals(attendance.getEvent(), returnedAttendance.getEvent());

    }


    @Test
    public void save_thenChangeStatus_shouldReturn_newAttendanceStatus() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Adrian", "Adrian12@hotmail.com", "hutablage14"));
        Calendar calendar = calendarRepository.save(new Calendar("EVN-Jahresablesung", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("jahresablesung 2021", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        AttendanceStatus attendanceEntity = new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING);
        AttendanceStatus updatedAttendanceEntity = new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED);
        AttendanceStatus savedAttendance = attendanceService.create(attendanceEntity);
        AttendanceStatus updatedAttendance = attendanceService.create(updatedAttendanceEntity);
        assertEquals(updatedAttendance.getUser(), savedAttendance.getUser());
        assertEquals(updatedAttendance.getUser(), attendanceEntity.getUser());
        assertEquals(updatedAttendance.getEvent(), savedAttendance.getEvent());
        assertEquals(updatedAttendance.getEvent(), attendanceEntity.getEvent());
        assertEquals(updatedAttendance.getStatus(), updatedAttendanceEntity.getStatus());
        assertNotEquals(updatedAttendance.getStatus(), savedAttendance.getStatus());
    }

    @Test
    public void when_UserAttend_getUsersByEvent_shouldReturnListContainingUser() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Bettina", "betti@protonmail.com", "hsuhwl1234jxufh238d"));
        Calendar calendar = calendarRepository.save(new Calendar("AAWienCollectiv", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Erster Sitzungstermin", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        List users = attendanceService.getUsersByEvent(event);
        assert (users.size() > 0);
    }

    @Test
    public void when_savedUser_findAllUsers_shouldReturnCorrectUserDetails() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Celvin", "celvin@gmx.at", "celvinisaweirdunit"));
        Calendar calendar = calendarRepository.save(new Calendar("Inventions", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Thermometer 2.0", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        ApplicationUser returnedUser = attendanceService.getUsersByEvent(event).get(0);
        assert (returnedUser.getId() != null && returnedUser.getId() != 0);

    }

    @Test
    public void when_UserAttendsEvent_getEventByUser_shouldReturnListContainingEvent() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Elisabeth Eiper", "eipi@aon.at", "butzibaby"));
        Calendar calendar = calendarRepository.save(new Calendar("Geburtstage", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Mamas Geburtstag", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        List events = attendanceService.getEventByUser(user);
        assert (events.size() > 0);
    }

    @Test
    public void when_UserAttendsEvent_getEventByUser_shouldReturnCorrectUserDetails() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Franc", "fg@gmx.com", "verververre"));
        Calendar calendar = calendarRepository.save(new Calendar("Movies", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Pulp Fiction", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        Event returnedEvent = attendanceService.getEventByUser(user).get(0);
        assert (returnedEvent.getId() != null && returnedEvent.getId() != 0);
        // StatusDto statusDto = mapper.applicationStatusToStatusDto(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        //  System.out.println(statusDto);
    }

    @Test
    public void when_UserDeclinesEvent_getEventUserIsAttending_shouldReturn_emptyList() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Gerald", "thewitcher@wildhunt.com", "hrmmmmmmmmm"));
        Calendar calendar = calendarRepository.save(new Calendar("Quent Tournament", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Quent Finals", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        Event event2 = eventRepository.save(new Event("Quent Semifinals", LocalDateTime.of(2021, 2, 1, 15, 30), LocalDateTime.of(2021, 2, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED));
        attendanceService.create(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.INTERESTED));
        List<Event> events = attendanceService.getEventUserIsAttending(user.getId());
        assert (events.size() == 0);
    }

    @Test
    public void when_UserAttendsEvent_getEventUserIsAttending_shouldReturn_correctList() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Hulk Hogan", "thehulkhogan@www.com", "wrestlingistotallyreal"));
        Calendar calendar = calendarRepository.save(new Calendar("Fake Wrestling Tips", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Punching without hurting yourself", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        Event event2 = eventRepository.save(new Event("Punching without hurting the other guy", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        attendanceService.create(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.DECLINED));
        List<Event> events = attendanceService.getEventUserIsAttending(user.getId());
        assert (events.size() == 1);
    }

    @Test
    public void when_userIsInterestedInEvents_getEventsUserIsInterested_shouldReturn_emptyList() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Iris Federsel", "ifs@tuwien.at", "jfui2hjöaspuihf3"));
        Calendar calendar = calendarRepository.save(new Calendar("Thermodynamik WS 2021", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("TD1", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        Event event2 = eventRepository.save(new Event("TD2", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        Event event3 = eventRepository.save(new Event("TD3", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        Event event4 = eventRepository.save(new Event("unschaffbare prüfung", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.ATTENDING));
        attendanceService.create(new AttendanceStatus(user, event2, AttendanceStatusPossibilities.INTERESTED));
        attendanceService.create(new AttendanceStatus(user, event3, AttendanceStatusPossibilities.INTERESTED));
        attendanceService.create(new AttendanceStatus(user, event4, AttendanceStatusPossibilities.DECLINED));
        List<Event> events = attendanceService.getEventUserIsInterested(user.getId());
        assert (events.size() == 2);
    }

    @Test
    public void when_userNotInterestedInEvents_getEventsUserIsInterested_shouldReturn_emptyList() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Jaqueline", "jaque@myspace.com", "kuhlimuhli"));
        Calendar calendar = calendarRepository.save(new Calendar("Breaking Bad Binching", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Start and Finish of 9 Seasons", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED));
        List<Event> events = attendanceService.getEventUserIsInterested(user.getId());
        assert (events.size() == 0);
    }

    @Test
    public void getUsersAttendingEvent_shouldReturn_onlyAttendingUsers() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Karl", "kjs@kabsi.at", "diebutter"));
        ApplicationUser user2 = userRepository.save(new ApplicationUser("Johann", "josef@karin.heast", "deputa"));
        ApplicationUser user3 = userRepository.save(new ApplicationUser("Anna", "chloroformhandel@realapotheke.com", "parswurt"));
        ApplicationUser user4 = userRepository.save(new ApplicationUser("Maria", "sictumsemper@hp.com", "spagetthi"));
        Calendar calendar = calendarRepository.save(new Calendar("Family", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Osterdinner", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED));
        attendanceService.create(new AttendanceStatus(user2, event, AttendanceStatusPossibilities.ATTENDING));
        attendanceService.create(new AttendanceStatus(user3, event, AttendanceStatusPossibilities.INTERESTED));
        attendanceService.create(new AttendanceStatus(user4, event, AttendanceStatusPossibilities.ATTENDING));
        List<ApplicationUser> users = attendanceService.getUsersAttendingEvent(event.getId());
        assert (users.size() == 2);
    }

    @Test
    public void getUsersInterestedInEvent_shouldReturn_onlyInterestedUsers() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Ludwig", "beethoven@musik.what", "icanthear"));
        ApplicationUser user2 = userRepository.save(new ApplicationUser("Martin", "skorsisi@films.com", "explosiongoesboom"));
        ApplicationUser user3 = userRepository.save(new ApplicationUser("Nathalie", "portman@icloud.com", "123456789"));
        ApplicationUser user4 = userRepository.save(new ApplicationUser("Oma", "oma@aol.com", "Lieblinsenkelkindistdaserste"));
        Calendar calendar = calendarRepository.save(new Calendar("Brotbacken für Anfänger", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Brot essen", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED));
        attendanceService.create(new AttendanceStatus(user2, event, AttendanceStatusPossibilities.ATTENDING));
        attendanceService.create(new AttendanceStatus(user3, event, AttendanceStatusPossibilities.INTERESTED));
        attendanceService.create(new AttendanceStatus(user4, event, AttendanceStatusPossibilities.ATTENDING));
        List<ApplicationUser> users = attendanceService.getUsersInterestedInEvent(event.getId());
        assert (users.size() == 1);
    }

    @Test
    public void getUsersDecliningEvent_shouldReturn_onlyInterestedUsers() {
        ApplicationUser user = userRepository.save(new ApplicationUser("Pope Franciscus", "holymoly@god.com", "godisdead4real"));
        ApplicationUser user2 = userRepository.save(new ApplicationUser("Qualle", "qualle@meer.it", "spongebobpartyisbestparty"));
        ApplicationUser user3 = userRepository.save(new ApplicationUser("Rasputin", "lovemachine@russia.com", "Blyatblyat"));
        ApplicationUser user4 = userRepository.save(new ApplicationUser("Superman", "clarkKent@newspaper.com", "Iambatman"));
        Calendar calendar = calendarRepository.save(new Calendar("Movies", Collections.singletonList(organization)));
        Event event = eventRepository.save(new Event("Pulp Fiction", LocalDateTime.of(2021, 1, 1, 15, 30), LocalDateTime.of(2021, 1, 1, 16, 0), calendar));
        attendanceService.create(new AttendanceStatus(user, event, AttendanceStatusPossibilities.DECLINED));
        attendanceService.create(new AttendanceStatus(user2, event, AttendanceStatusPossibilities.ATTENDING));
        attendanceService.create(new AttendanceStatus(user3, event, AttendanceStatusPossibilities.INTERESTED));
        attendanceService.create(new AttendanceStatus(user4, event, AttendanceStatusPossibilities.ATTENDING));
        List<ApplicationUser> users = attendanceService.getUsersDecliningEvent(event.getId());
        assert (users.size() == 1);
    }


}
