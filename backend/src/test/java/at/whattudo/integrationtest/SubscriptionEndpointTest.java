package at.whattudo.integrationtest;


import at.whattudo.endpoint.SubscriptionEndpoint;
import at.whattudo.endpoint.dto.SubscriptionDto;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.OrganizationRepository;
import at.whattudo.repository.SubscriptionRepository;
import at.whattudo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class SubscriptionEndpointTest {

    public Organization organization;
    @Autowired
    SubscriptionEndpoint subscriptionEndpoint;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    OrganizationRepository organizationRepository;

    public SubscriptionDto createMockDtoWithData() {
        organization = organizationRepository.save(new Organization("TestOrg"));
        ApplicationUser user = userRepository.findByName("Dillon Dingle").get();
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        Calendar calendar = new Calendar("Test Calendar", organizations);

        ApplicationUser savedUser = userRepository.save(user);
        Calendar savedCalendar = calendarRepository.save(calendar);

        return new SubscriptionDto(0, savedUser.getName(), savedCalendar.getId());
    }

    public void clean() {
        organizationRepository.delete(organization);
    }

    @WithMockUser(username = "Dillon Dingle")
    @Test
    @Transactional
    public void createSubscription_shouldReturnSubscription() {
        SubscriptionDto subscriptionDto = createMockDtoWithData();
        SubscriptionDto savedDto = subscriptionEndpoint.create(subscriptionDto);

        assertNotNull(savedDto);
        assertEquals(subscriptionDto.calendarId, savedDto.calendarId);
        assertEquals(subscriptionDto.userName, savedDto.userName);
        clean();
    }

    @WithMockUser(username = "Dillon Dingle")
    @Test
    public void createSubscription_thenDeleteSubscription_thenCallGetAllForUser_shouldReturnEmptyList() {
        SubscriptionDto saved = subscriptionEndpoint.create(createMockDtoWithData());
        SubscriptionDto deleted = subscriptionEndpoint.delete(saved.id);

        assertNotNull(deleted);
        ApplicationUser user = userRepository.findByName("Dillon Dingle").get();
        List<SubscriptionDto> subscriptionDtos = subscriptionEndpoint.getSubscriptionsForuser(user.getId());

        assert (!subscriptionDtos.contains(deleted));
        clean();
    }

    @WithMockUser(username = "Dillon Dingle")
    @Test
    public void getSubscriptionsForUser_shouldReturnSubscriptionListForUser() {
        SubscriptionDto saved = subscriptionEndpoint.create(createMockDtoWithData());
        ApplicationUser user = userRepository.findByName(saved.getUserName()).get();
        List<SubscriptionDto> subscriptionDtos = subscriptionEndpoint.getSubscriptionsForuser(user.getId());
        assertNotEquals(0, subscriptionDtos.size());

        subscriptionDtos.forEach(dto -> {
            assertEquals(user.getName(), dto.getUserName());
        });
        clean();
    }

    @WithMockUser(username = "Dillon Dingle")
    @Test
    public void getSubscriptionsForCalendar_shouldReturnSubscriptionListForCalendar() {
        SubscriptionDto saved = subscriptionEndpoint.create(createMockDtoWithData());
        Calendar calendar = calendarRepository.findById(saved.getCalendarId()).get();
        List<SubscriptionDto> subscriptionDtos = subscriptionEndpoint.getSubscriptionsForCalendar(saved.calendarId);
        assertNotEquals(0, subscriptionDtos.size());

        subscriptionDtos.forEach(dto -> {
            assertEquals(calendar.getId(), dto.getCalendarId());
        });
        clean();
    }
}
