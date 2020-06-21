package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LabelMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = LocationEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LocationEndpoint {
    static final String BASE_URL = "/locations";
    private final LocationService locationService;
    private final LocationMapper locationMapper;


    @PreAuthorize("permitAll()")
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all locations", authorizations = {@Authorization(value = "apiKey")})
    public List<LocationDto> getAllLocations() {
        try {
            return locationService.getAll().stream()
                .map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public LocationDto getById(@PathVariable("id") Integer id) {
        try {
            return locationMapper.locationToLocationDto(locationService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create location", authorizations = {@Authorization(value = "apiKey")})
    public LocationDto create(@RequestBody LocationDto dto) {
        try {
            List<Integer> eventsShouldBeEmpty = new ArrayList<>();
            dto.setEventIds(eventsShouldBeEmpty);
            Location location = locationMapper.locationDtoToLocation(dto);
            return locationMapper.locationToLocationDto(locationService.save(location));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @ApiOperation(value = "Update label", authorizations = {@Authorization(value = "apiKey")})
    public LocationDto update(@RequestBody LocationDto dto) {
        try {
            Location location = locationMapper.locationDtoToLocation(dto);
            return locationMapper.locationToLocationDto(locationService.update(location));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete label", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable("id") Integer id) {
        try {
            locationService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
