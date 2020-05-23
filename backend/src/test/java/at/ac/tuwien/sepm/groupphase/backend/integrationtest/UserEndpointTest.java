package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.ChangeUserPasswordEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.UserEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
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
        UserDto userDto = new UserDto(0, "Test", "testy@test.com", "hunter2");

        UserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());
        assertNotEquals(userDto.getId(), savedUserDto.getId());
        assertNotEquals(userDto.getPassword(), savedUserDto.getPassword());

    }

    @Test
    public void updateUser(){
        UserDto userDto = new UserDto(null, "user1", "testy@test.com", "hunter2");

        UserDto savedUserDto = userEndpoint.createNewUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());

        UserDto userDto1 = new UserDto(savedUserDto.getId(), "user2", null, null);

        UserDto updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(userDto1.getName(), updateUser.getName());


         userDto1 = new UserDto(savedUserDto.getId(), null, "user43@test.com", null);

        updateUser = userEndpoint.updateUser(userDto1);

        assertEquals(savedUserDto.getId(), updateUser.getId());
        assertEquals(userDto1.getEmail(), updateUser.getEmail());


    }

    @Test
    public void changePassword(){
        UserDto userDto = new UserDto(null, "changePasswordUser", "changepass@test.com", "hunter3");

        UserDto savedUserDto = userEndpoint.createNewUser(userDto);

        UserDto changePasswordUserDto =  userPasswordEndpoint.changeUserPassword(savedUserDto.getEmail(), "hunter3", "hunter4");

        assertTrue(passwordEncoder.matches("hunter4", changePasswordUserDto.getPassword()));


    }



}
