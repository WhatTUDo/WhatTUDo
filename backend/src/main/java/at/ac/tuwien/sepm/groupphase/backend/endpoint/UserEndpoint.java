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
import java.util.stream.Collectors;

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

    @PreAuthorize("hasRole('SYSADMIN')")
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all users")
    public List<LoggedInUserDto> getAllUsers() {
        try {
            List<ApplicationUser> users = userService.getAllUsers();

            return users.stream().map(userMapper::applicationUserToUserDto).collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New User!")
    public LoggedInUserDto createNewUser(@RequestBody IncomingUserDto dto) {
        try {
            ApplicationUser newUser = userService.saveNewUser(userMapper.userDtoToApplicationUser(dto));

            return userMapper.applicationUserToUserDto(newUser);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SYSADMIN') || #dto.name == authentication.name")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto updateUser(@RequestBody LoggedInUserDto dto) {
        try {
            return userMapper.applicationUserToUserDto(userService.updateUser(userMapper.loggedInUserDtoToApplicationUser(dto)));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage()); //FIXME return empty array?
        }
    }

    @PreAuthorize("hasPermission(#orgaId, 'ORGA', 'MOD')")
    @PutMapping("/{id}/roles")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto updateRoleInOrga(@PathVariable Integer id, @RequestParam Integer orgaId, @RequestParam String role) {
        try {
            return userMapper.applicationUserToUserDto(userService.updateRoleInOrga(
                userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")),
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
    @DeleteMapping("/{id}/roles")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("update user")
    public LoggedInUserDto removeFromOrga(@PathVariable Integer id, @RequestParam Integer orgaId) {
        try {
            return userMapper.applicationUserToUserDto(userService.removeFromOrga(
                userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")),
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
    @ApiOperation(value = "Get logged user")
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
    public List<OrganizationDto> getOrganizationOfUser(@PathVariable(value = "id") Integer id) {
        try {
            log.info("get organizations of user with id {}", id);
            List<OrganizationDto> organizationDtos = new ArrayList<>();
            List<Organization> organizations = userService.getUserOrganizations(id);
            for (Organization o : organizations
            ) {
                organizationDtos.add(organizationMapper.organizationToOrganizationDto(o));
            }
            return organizationDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping("/getByName/{name}")
    @ApiOperation(value = "Get user by name", authorizations = {@Authorization(value = "apiKey")})
    public LoggedInUserDto getUserByName(@PathVariable(value = "name") String name){
     try  { ApplicationUser applicationUser = userService.getUserByName(name);
        return userMapper.applicationUserToUserDto(applicationUser);
     } catch (ServiceException e){
         throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
     }
    }



    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/recommendedEvents/{id}")
    @ApiOperation(value = "Get recommended Events")
    public List<EventDto> getRecommendedEvents(@PathVariable(value = "id") Integer id) {
        log.info("get recommended event for user");

        try {
            List<Event> recommendedEvent = userService.getRecommendedEvents(id);
            if (recommendedEvent.size() < 4) {
                ArrayList ids = new ArrayList();
                for (Event e : recommendedEvent
                ) {
                    ids.add(e.getId());
                }
                for (int i = 0; i < 4 - recommendedEvent.size(); i++) {
                    Optional<Event> event = eventService.findForDates(LocalDateTime.now(), LocalDateTime.now().plusMonths(6)).stream().findAny();
                    if (event.isPresent() && !ids.contains(event.get().getId())) recommendedEvent.add(event.get());
                }
                for (int i = 0; i < 4 - recommendedEvent.size(); i++) {
                    Optional<Event> event = eventService.findForDates(LocalDateTime.now(), LocalDateTime.MAX).stream().findAny();
                    if (event.isPresent() && !ids.contains(event.get().getId())) recommendedEvent.add(event.get());
                }
            }
            List<EventDto> eventDtos = new ArrayList<>();
            recommendedEvent.forEach(event -> eventDtos.add(eventMapper.eventToEventDto(event)));
            return eventDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            return null;
        }
    }
}
