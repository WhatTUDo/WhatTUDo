package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganisationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrganisationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
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


@Slf4j
@RestController
@RequestMapping(value = OrganisationEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrganisationEndpoint {
    static final String BASE_URL = "/organisations";
    private final OrganisationService organisationService;
    private final OrganisationMapper organisationMapper;


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit organisation", authorizations = {@Authorization(value = "apiKey")})
    public OrganisationDto editOrganisation(@RequestBody OrganisationDto organisation) {
        log.info("PUT " + BASE_URL + "/{}", organisation);
        try {
            Organisation organisationEntity = organisationMapper.dtoToEntity(organisation);
            return organisationMapper.entityToDto(organisationService.update(organisationEntity));
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

}
