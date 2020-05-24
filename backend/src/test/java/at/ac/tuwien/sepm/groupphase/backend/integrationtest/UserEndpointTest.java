package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.ChangeUserPasswordEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserEndpointTest {
    @Autowired
    UserEndpoint userEndpoint;

    @Autowired
    ChangeUserPasswordEndpoint userPasswordEndpoint;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void saveNewUser_shouldReturn_UserDto_withEncodedPassword() {
        IncomingUserDto userDto = new IncomingUserDto(0, "Test", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());
        assertNotEquals(userDto.getId(), savedUserDto.getId());
//        assertNotEquals(userDto.getPassword(), savedUserDto.getPassword());

    }

    @Test
    public void updateUser(){
        IncomingUserDto userDto = new IncomingUserDto(null, "user1", "testy@test.com", "hunter2");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());

        LoggedInUserDto userDto1 = new LoggedInUserDto(savedUserDto.getId(), "user2", null);

        LoggedInUserDto updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(userDto1.getName(), updateUser.getName());


         userDto1 = new LoggedInUserDto(savedUserDto.getId(), null, "user43@test.com");

        updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(savedUserDto.getId(), updateUser.getId());
        assertEquals(userDto1.getEmail(), updateUser.getEmail());


    }

    @Test
    public void changePassword(){
        IncomingUserDto userDto = new IncomingUserDto(null, "changePasswordUser", "changepass@test.com", "hunter3");

        LoggedInUserDto savedUserDto = userEndpoint.createNewUser(userDto);

        LoggedInUserDto changePasswordUserDto =  userPasswordEndpoint.changeUserPassword(new ChangePasswordDto(savedUserDto.getEmail(), "hunter3", "hunter4"));

//        assertTrue(passwordEncoder.matches("hunter4", changePasswordUserDto.getPassword()));


    }



}
