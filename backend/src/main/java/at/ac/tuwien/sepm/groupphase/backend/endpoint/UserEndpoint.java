package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(value = UserEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserEndpoint {
    static final String BASE_URL = "/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("permitAll()")
    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New User!")
    public LoggedInUserDto createNewUser(@RequestBody IncomingUserDto user) {
        try {
            ApplicationUser newUser = userService.saveNewUser(userMapper.userDtoToApplicationUser(user));

            return userMapper.applicationUserToUserDto(newUser);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SYSADMIN') || #userDto.name == principal.username")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto updateUser(@RequestBody LoggedInUserDto userDto){
        try {
            return userMapper.applicationUserToUserDto(userService.updateUser(userMapper.loggedInUserDtoToApplicationUser(userDto)));
        } catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage()); //FIXME return empty array?
        }
    }
}
