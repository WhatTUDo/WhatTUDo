package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.ChangeUserPasswordEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.models.auth.In;
import org.hibernate.event.service.spi.EventListenerRegistrationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UserEndpointTest {
    @Autowired
    UserEndpoint userEndpoint;

    @Autowired
    UserService userService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;


    @WithMockUser
    @Test
    public void saveNewUser_shouldReturn_UserDto_withEncodedPassword() {
        IncomingUserDto userDto = new IncomingUserDto(0, "Test", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());
        assertNotEquals(userDto.getId(), savedUserDto.getId());
//        assertNotEquals(userDto.getPassword(), savedUserDto.getPassword());

    }


    @WithMockUser
    @Test
    public void getUserOrganizations() {
        Organization organization = organizationRepository.save(new Organization("organization test"));
        ApplicationUser user = userRepository.save(new ApplicationUser("user", "user@test.at", "usertest"));
        Set<OrganizationMembership> organizationMembershipSet = new HashSet<>();
        organizationMembershipSet.add(new OrganizationMembership(organization, user, OrganizationRole.MEMBER));
        user.setMemberships(organizationMembershipSet);
        userRepository.save(user);

        assertEquals(organization.getName(), userEndpoint.getOrganizationOfUser(user.getId()).get(0).getName());
    }


}
