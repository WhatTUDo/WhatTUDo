package at.ac.tuwien.sepm.groupphase.backend.servicetests;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SubscriptionServiceTest {

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    UserService userService;

    @Test
    public void createNewSubscirption_shouldReturnNewSubscription() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);

        Subscription newSubscription = new Subscription(0, savedUser, savedCalendar);

        Subscription savedSubscription = subscriptionService.create(newSubscription);

        assertNotNull(savedSubscription);
        assertEquals(newSubscription.getUser().getName(), savedSubscription.getUser().getName());
        assertEquals(newSubscription.getCalendar().getName(), savedSubscription.getCalendar().getName());
    }

    @Test
    public void createNewSubscription_withUnsavedCalendar_shouldThrowNotFoundException() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar newCalendar = new Calendar("TestCal", organizations, "Lorem Ipsum");
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));

        assertThrows(NotFoundException.class, () -> {
            subscriptionService.create(new Subscription(0, savedUser, newCalendar));
        });
    }

    @Test
    public void createNewSubscription_withUnsavedUser_shouldThrowNotFoundException() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser newUser = new ApplicationUser("TestName", "test@mail.com", "alligator1");

        assertThrows(NotFoundException.class, () -> {
            subscriptionService.create(new Subscription(0, newUser, savedCalendar));
        });
    }

    @Test
    public void saveSubscription_thenRetrieveById_shouldReturnSubscription() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);

        Subscription savedSubscription = subscriptionService.create(new Subscription(0, savedUser, savedCalendar));

        Subscription retrievedSubscription = subscriptionService.getById(savedSubscription.getId());

        assertEquals(savedSubscription.getId(), retrievedSubscription.getId());
        assertEquals(savedSubscription.getUser().getName(), retrievedSubscription.getUser().getName());
        assertEquals(savedSubscription.getCalendar().getName(), retrievedSubscription.getCalendar().getName());
    }


    @Test
    public void saveSubscription_thenRetrieveAllByUser_shouldReturnListWithSubscriptionIncluded() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);
        Subscription savedSubscription = subscriptionService.create(new Subscription(0, savedUser, savedCalendar));
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUser(savedUser);
        assertNotEquals(0, subscriptions.size());
        assertEquals(subscriptions.get(0).getUser().getName(), savedSubscription.getUser().getName());
    }


}
