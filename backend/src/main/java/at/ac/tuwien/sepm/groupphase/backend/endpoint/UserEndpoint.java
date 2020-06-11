package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrganizationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = UserEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserEndpoint {
    static final String BASE_URL = "/users";
    private final UserService userService;
    private final EventService eventService;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final OrganizationMapper organizationMapper;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @PreAuthorize("permitAll()")
    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New User!")
    public LoggedInUserDto createNewUser(@RequestBody IncomingUserDto user) {
        try {
            ApplicationUser newUser = userService.saveNewUser(userMapper.userDtoToApplicationUser(user));

            return userMapper.applicationUserToUserDto(newUser);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SYSADMIN') || #userDto.name == principal.username")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto updateUser(@RequestBody LoggedInUserDto userDto) {
        try {
            return userMapper.applicationUserToUserDto(userService.updateUser(userMapper.loggedInUserDtoToApplicationUser(userDto)));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage()); //FIXME return empty array?
        }
    }

    @PreAuthorize("hasPermission(#orgaId, 'ORGA', 'MOD')")
    @PutMapping("/{userId}/roles")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto updateRoleInOrga(@PathVariable Integer userId, @RequestParam Integer orgaId, @RequestParam String role) {
        try {
            return userMapper.applicationUserToUserDto(userService.updateRoleInOrga(
                userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")),
                organizationRepository.findById(orgaId).orElseThrow(() -> new NotFoundException("Organization not found")),
                OrganizationRole.valueOf(role)
            ));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasPermission(#orgaId, 'ORGA', 'MOD')")
    @DeleteMapping("/{userId}/roles")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto removeFromOrga(@PathVariable Integer userId, @RequestParam Integer orgaId) {
        try {
            return userMapper.applicationUserToUserDto(userService.removeFromOrga(
                userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")),
                organizationRepository.findById(orgaId).orElseThrow(() -> new NotFoundException("Organization not found"))
            ));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping("/user")
    @ApiOperation(value = "Get logged user", authorizations = {@Authorization(value = "apiKey")})
    public LoggedInUserDto getLoggedUser() {
        try {
            log.info("get current user");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication instanceof AnonymousAuthenticationToken)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "some message");
            }
            return userMapper.applicationUserToUserDto(userService.getUserByName(authentication.getName()));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping("/organizations/{id}")
    @ApiOperation(value = "Get organization user is member", authorizations = {@Authorization(value = "apiKey")})
    public List<OrganizationDto> getOrganizationOfUser(@PathVariable(value = "id") Integer userId) {
        try {
            log.info("get organizations of user with id {}",userId);
            List<OrganizationDto> organizationDtos = new ArrayList<>();
            List<Organization> organizations = userService.getUserOrganizations(userId);
            for (Organization o: organizations
                 ) {
               organizationDtos.add( organizationMapper.organizationToOrganizationDto(o));
            }
            return organizationDtos;
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/recommendedEvent/{id}")
    @ApiOperation(value = "Get recommended Event", authorizations = {@Authorization(value = "apiKey")})
    public EventDto getRecommendedEvent(@PathVariable(value = "id") Integer userId) {
        log.info("get recommended event for user");

        try {
            Optional<Event> recommendedEvent = userService.getRecommendedEvent(userId);
            if (recommendedEvent.isPresent()) {
                return eventMapper.eventToEventDto(recommendedEvent.get());
            } else {
                Optional<Event> events = eventService.findForDates(LocalDateTime.now(), LocalDateTime.now().plusDays(30)).stream().findAny();
                if (events.isPresent()) {
                    return eventMapper.eventToEventDto(events.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.OK, "No recommendable events found");
                }
            }
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            return null;
        }
    }


}
