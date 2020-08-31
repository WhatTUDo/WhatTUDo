package at.whattudo.servicetests;

import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.entity.Subscription;
import at.whattudo.exception.NotFoundException;
import at.whattudo.service.CalendarService;
import at.whattudo.service.OrganizationService;
import at.whattudo.service.SubscriptionService;
import at.whattudo.service.UserService;
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
        organizationService.delete(organization.getId());
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
        organizationService.delete(organization.getId());

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
        organizationService.delete(organization.getId());

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
        organizationService.delete(organization.getId());

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
        organizationService.delete(organization.getId());

    }

    @Test
    public void saveSubscription_thenRetrieveAllByCalendar_shouldReturnListWithSubscriptionIncluded() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);
        Subscription savedSubscription = subscriptionService.create(new Subscription(0, savedUser, savedCalendar));
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsForCalendar(savedCalendar);
        assertNotEquals(0, subscriptions.size());
        assertEquals(subscriptions.get(0).getUser().getName(), savedSubscription.getUser().getName());
        organizationService.delete(organization.getId());

    }

    @Test
    public void retrieveAllByCalendar_shouldReturnEmptyList() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);
        //note: no sub save
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsForCalendar(savedCalendar);
        assertEquals(0, subscriptions.size());
        organizationService.delete(organization.getId());

    }

    @Test
    public void saveSubscription_thenDelete_thenRetrieveAllByUser_shouldReturnEmptyList() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);

        Subscription newSubscription = new Subscription(0, savedUser, savedCalendar);
        Subscription savedSubscription = subscriptionService.create(newSubscription);
        subscriptionService.delete(savedSubscription);
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUser(savedUser);
        assertEquals(0, subscriptions.size());
        organizationService.delete(organization.getId());
    }

    @Test
    public void deleteUnsavedSubscription_shouldThrowNotFoundException() {
        Organization organization = organizationService.create(new Organization("TestOrga"));
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar savedCalendar = calendarService.save(new Calendar("TestCal", organizations, "Lorem Ipsum"));
        ApplicationUser savedUser = userService.saveNewUser(new ApplicationUser("TestName", "test@mail.com", "alligator1"));
        assertNotNull(savedCalendar);
        assertNotNull(savedUser);

        Subscription newSubscription = new Subscription(0, savedUser, savedCalendar);
        assertThrows(NotFoundException.class, () -> subscriptionService.delete(newSubscription));
        organizationService.delete(organization.getId());
    }
}
