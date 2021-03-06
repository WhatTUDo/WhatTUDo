package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrganizationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import com.google.common.io.ByteStreams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = OrganizationEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrganizationEndpoint {
    public static final String BASE_URL = "/organizations";
    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper;
    private final CalendarService calendarService;
    private final UserService userService;


    @PreAuthorize("hasPermission(#dto, 'ADMIN')")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit organization", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto editOrganization(@RequestBody OrganizationDto dto) {
        try {
            Organization organizationEntity = organizationService.findById(dto.getId());
            organizationEntity.setName(dto.getName());
            organizationEntity.setDescription(dto.getDescription());
            organizationMapper.mapCalendars(dto, organizationEntity);
            return organizationMapper.organizationToOrganizationDto(organizationService.update(organizationEntity));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('SYSADMIN')")
    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create Organization", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto createOrganization(@RequestBody OrganizationDto dto) {
        try {
            Organization organizationEntity = organizationMapper.organizationDtoToOrganization(dto);
            return organizationMapper.organizationToOrganizationDto(organizationService.create(organizationEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all Organizations", authorizations = {@Authorization(value = "apiKey")})
    public List<OrganizationDto> getAllOrgas() {
        try {
            return organizationService.getAll().stream()
                .map(organizationMapper::organizationToOrganizationDto)
                .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto getOrgaById(@PathVariable(value = "id") Integer id) {
        try {
            return organizationMapper.organizationToOrganizationDto(organizationService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Search Organizations with name (or name fragment)", authorizations = {@Authorization(value = "apiKey")})
    public List<OrganizationDto> searchOrganizationNames(@RequestParam("name") String name) {
        try {
            return organizationMapper.organizationListToorganizationDtoList(organizationService.searchForName(name));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PreAuthorize("hasPermission(#id, 'ORGA', 'ADMIN')")
    @CrossOrigin
    @Transactional
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete Organization", authorizations = {@Authorization(value = "apiKey")})
    public Integer delete(@PathVariable(value = "id") Integer id) {
        try {

             return this.organizationService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'ORGA', 'MOD')")
    // TODO: Check if other organization allow it (maybe invite system?)
    @PutMapping(value = "/{id}/calendars")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Calendars to an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto addCalToOrga(@PathVariable(value = "id") Integer id, @RequestParam(value = "id") List<Integer> calendarIds) {
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.addCalendars(organizationService.findById(id), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'ORGA', 'MOD')")
    // TODO: Check if other organization allow it (maybe invite system?)
    @DeleteMapping(value = "/{id}/calendars")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Calendars from an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto removeCalFromOrga(@PathVariable(value = "id") Integer id, @RequestParam(value = "id") List<Integer> calendarIds) {
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.removeCalendars(organizationService.findById(id), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/members/{id}")
    @CrossOrigin
    @ApiOperation(value = "get organization members", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getOrganizationMembers(@PathVariable(value = "id") Integer id) {
        try {
            log.info("get members of organization with id {}", id);
            List<LoggedInUserDto> userDtos = new ArrayList<>();
            List<ApplicationUser> users = organizationService.getMembers(id);
            for (ApplicationUser user : users) {
                userDtos.add(userMapper.applicationUserToUserDto(user));
            }
            return userDtos;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }

    }

    @PreAuthorize("hasPermission(#id, 'ORGA', 'MOD')")
    @PutMapping(value = "/addMembership/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "create membership", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto addMembership(@PathVariable(value = "id") Integer id,@RequestParam(value = "userId") Integer userId, @RequestParam(value = "role") String role ){
        try {
            ApplicationUser applicationUser = userService.findUserById(userId);
            Organization organization = organizationService.findById(id);
            if(role.equals("Moderator")){
                role = "MOD";
            }
            return organizationMapper.organizationToOrganizationDto(organizationService.addMembership(applicationUser,organization, role.toUpperCase()));
        }catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get cover image", authorizations = {@Authorization(value = "apiKey")})
    public @ResponseBody byte[] getCoverImage(@PathVariable int id) {
        try {
            Byte[] coverImageBlob = organizationService.findById(id).getCoverImage();
            byte[] byteArray;
            if (coverImageBlob != null) {
                byteArray = new byte[coverImageBlob.length];
                for (int i = 0; i < coverImageBlob.length; i++) byteArray[i] = coverImageBlob[i];
            } else {
                try {
                    InputStream stream = OrganizationEndpoint.class.getResourceAsStream("/images/default-orga-background.jpg");
                    //noinspection UnstableApiUsage
                    byteArray = ByteStreams.toByteArray(stream);
                } catch (IOException e) {
                    byteArray = new byte[0];
                }
            }
            return byteArray;
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#id, 'ORGA', 'ADMIN')")
    @CrossOrigin
    @PostMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Set cover image", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto setCoverImage(@PathVariable int id, @RequestParam("imagefile") MultipartFile file) {
        try {
            Organization organization = organizationService.setCoverImage(organizationService.findById(id), file.getBytes());
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), e);
        }
    }
}
