package at.whattudo.integrationtest;


import at.whattudo.endpoint.UserEndpoint;
import at.whattudo.endpoint.dto.IncomingUserDto;
import at.whattudo.endpoint.dto.LoggedInUserDto;
import at.whattudo.entity.*;
import at.whattudo.repository.*;
import at.whattudo.repository.AttendanceRepository;
import at.whattudo.repository.OrganizationRepository;
import at.whattudo.repository.UserRepository;
import at.whattudo.service.UserService;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Organization;
import at.whattudo.entity.OrganizationMembership;
import at.whattudo.entity.OrganizationRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @WithMockUser(username = "user 1")
    @Test
    public void updateUserName(){
        ApplicationUser user = userRepository.save(new ApplicationUser("user 1", "user@test.at", "usertest"));

        LoggedInUserDto updatedUser = userEndpoint.updateUser("user 1", new LoggedInUserDto(user.getId(), "user 3", ""));

        assertEquals("user 3", userRepository.findById(user.getId()).get().getName());
    }

    @WithMockUser(username = "user email")
    @Test
    public void updateUserEmail(){
        ApplicationUser user = userRepository.save(new ApplicationUser("user email", "useremail@test.at", "usertest"));

        LoggedInUserDto updatedUser = userEndpoint.updateUser("user email", new LoggedInUserDto(user.getId(), "", "useremail1@test.at"));

        assertEquals("useremail1@test.at", userRepository.findById(user.getId()).get().getEmail());
    }


}
