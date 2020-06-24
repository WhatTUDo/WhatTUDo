package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChangePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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
@RequestMapping(value = ChangeUserPasswordEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChangeUserPasswordEndpoint {
    static final String BASE_URL = "/changePwd";
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('SYSADMIN') || #dto.username == authentication.name")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update User Password", authorizations = {@Authorization(value = "apiKey")})
    public LoggedInUserDto changeUserPassword(@RequestBody ChangePasswordDto dto) {
        try {
            return userMapper.applicationUserToUserDto(userService.changeUserPassword(
                dto.getUsername(),
                dto.getCurrentPassword(),
                dto.getNewPassword()
            ));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
