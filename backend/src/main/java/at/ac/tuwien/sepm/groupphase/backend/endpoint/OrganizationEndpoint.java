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


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit organization", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto editOrganization(@RequestBody OrganizationDto organization) {
        log.info("PUT " + BASE_URL + "/{}", organization); // FIXME: Macht keinen Sinn, Organization ist nicht in der URL
        try {
            // Organization organizationEntity = organizationMapper.organizationDtoToOrganization(organization);
            Organization organizationEntity = organizationService.findById(organization.getId());
            organizationEntity.setName(organization.getName());
            organizationMapper.mapCalendars(organization, organizationEntity);
            return organizationMapper.organizationToOrganizationDto(organizationService.update(organizationEntity));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create Organization", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto createOrganization(@RequestBody OrganizationDto organizationDto) {
        log.info("POST " + BASE_URL + "/");
        try {
            Organization organizationEntity = organizationMapper.organizationDtoToOrganization(organizationDto);
            return organizationMapper.organizationToOrganizationDto(organizationService.create(organizationEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }




    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all Organizations", authorizations = {@Authorization(value = "apiKey")})
    public List<OrganizationDto> getAllOrgas() {
        log.info("GET" + BASE_URL + "/");
        try {
            return organizationService.getAll().stream()
                .map(organizationMapper::organizationToOrganizationDto)
                .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto getOrgaById(@PathVariable(value = "id") Integer id) {
        log.info("GET" + BASE_URL + "/");
        try {
            return organizationMapper.organizationToOrganizationDto(organizationService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


    @PutMapping(value = "/{id}/calendars")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Calendars to an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto addCalToOrga(@PathVariable(value = "id") Integer orgaId, @RequestParam(value = "id") List<Integer> calendarIds) {
        log.info("PUT " + BASE_URL + "/{}/calendars", orgaId);
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.addCalendars(organizationService.findById(orgaId), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/{id}/calendars")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Calendars from an Organization by ID", authorizations = {@Authorization(value = "apiKey")})
    public OrganizationDto removeCalFromOrga(@PathVariable(value = "id") Integer orgaId, @RequestParam(value = "id") List<Integer> calendarIds) {
        log.info("DELETE " + BASE_URL + "/{}/calendars", orgaId);
        try {
            Collection<Calendar> calendars = calendarIds.stream().map(calendarService::findById).collect(Collectors.toList());
            Organization organization = organizationService.removeCalendars(organizationService.findById(orgaId), calendars);
            return organizationMapper.organizationToOrganizationDto(organization);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
