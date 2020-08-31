package at.whattudo.endpoint;

import at.whattudo.endpoint.dto.LabelDto;
import at.whattudo.endpoint.mapper.LabelMapper;
import at.whattudo.entity.Label;
import at.whattudo.exception.NotFoundException;
import at.whattudo.service.LabelService;
import at.whattudo.util.ValidationException;
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
@RequestMapping(value = LabelEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LabelEndpoint {
    static final String BASE_URL = "/labels";
    private final LabelService labelService;
    private final LabelMapper labelMapper;


    @PreAuthorize("permitAll()")
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public LabelDto getById(@PathVariable("id") Integer id) {
        try {
            return labelMapper.labelToLabelDto(labelService.findById(id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('SYSADMIN')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete label", authorizations = {@Authorization(value = "apiKey")})
    public void delete(@PathVariable("id") Integer id) {
        try {
            labelService.delete(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('SYSADMIN')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create label", authorizations = {@Authorization(value = "apiKey")})
    public LabelDto create(@RequestBody LabelDto dto) {
        try {
            List<Integer> eventsShouldBeEmpty = new ArrayList<Integer>();
            dto.setEventIds(eventsShouldBeEmpty);
            Label label = labelMapper.labelDtoToLabel(dto);
            return labelMapper.labelToLabelDto(labelService.save(label));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('SYSADMIN')")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @ApiOperation(value = "Update label", authorizations = {@Authorization(value = "apiKey")})
    public LabelDto update(@RequestBody LabelDto dto) {
        try {
            Label label = labelMapper.labelDtoToLabel(dto);
            return labelMapper.labelToLabelDto(labelService.update(label));
        } catch (ValidationException | IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all Labels", authorizations = {@Authorization(value = "apiKey")})
    public List<LabelDto> getAllLabels() {
        try {
            return labelService.getAll().stream()
                .map(labelMapper::labelToLabelDto)
                .collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


}
