package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrganizationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;

import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;



@Slf4j
@RestController
@RequestMapping(value = OrganizationEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrganizationEndpoint {
    static final String BASE_URL = "/organizations";
    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;
    private final CalendarService calendarService;


    @PreAuthorize("hasPermission(#organization, 'MOD')")
    @PutMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit organization", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto editOrganization(@RequestBody OrganizationDto organization) {
        try {
            Organization organizationEntity = organizationService.findById(organization.getId());
            organizationEntity.setName(organization.getName());
            organizationMapper.mapCalendars(organization, organizationEntity);
            return organizationMapper.organizationToOrganizationDto(organizationService.update(organizationEntity));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
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
    public OrganizationDto createOrganization(@RequestBody OrganizationDto organizationDto) {
        try {
            Organization organizationEntity = organizationMapper.organizationDtoToOrganization(organizationDto);
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
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e); //FIXME return empty array?
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PreAuthorize("hasPermission(#id, 'ORGA', 'MOD')")
    @DeleteMapping(value = "/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete Organization", authorizations = {@Authorization(value = "apiKey")})
    public Integer deleteOrga(@PathVariable(value = "id") Integer id) {
        try {
            return this.organizationService.delete(id);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
        catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.OK, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#orgaId, 'ORGA', 'MOD')") // We can use the ID instead of the DTO TODO: Check if other organization allow it (maybe invite system?)
    @PutMapping(value = "/{id}/calendars")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Calendars to an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto addCalToOrga(@PathVariable(value = "id") Integer orgaId, @RequestParam(value = "id") List<Integer> calendarIds) {
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.addCalendars(organizationService.findById(orgaId), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(#orgaId, 'ORGA', 'MOD')") // We can use the ID instead of the DTO TODO: Check if other organization allow it (maybe invite system?)
    @DeleteMapping(value = "/{id}/calendars")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Calendars from an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto removeCalFromOrga(@PathVariable(value = "id") Integer orgaId, @RequestParam(value = "id") List<Integer> calendarIds) {
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.removeCalendars(organizationService.findById(orgaId), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
