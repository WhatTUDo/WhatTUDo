package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.SubscriptionEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SubscriptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        ApplicationUser user = userRepository.findByName("Person 1").get();
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

    @WithMockUser(username = "Person 1")
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

    @WithMockUser(username = "Person 1")
    @Test
    public void createSubscription_thenDeleteSubscription_thenCallGetAllForUser_shouldReturnEmptyList() {
        SubscriptionDto saved = subscriptionEndpoint.create(createMockDtoWithData());
        SubscriptionDto deleted = subscriptionEndpoint.delete(saved.id);

        assertNotNull(deleted);
        ApplicationUser user = userRepository.findByName("Person 1").get();
        List<SubscriptionDto> subscriptionDtos = subscriptionEndpoint.getSubscriptionsForuser(user.getId());

        assertEquals(0, subscriptionDtos.size());
        clean();
    }
}
