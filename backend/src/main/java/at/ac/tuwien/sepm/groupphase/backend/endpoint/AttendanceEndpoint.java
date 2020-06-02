package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.StatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
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
@RequestMapping(value = AttendanceEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AttendanceEndpoint {
    static final String BASE_URL = "/attendance";
    private final AttendanceService attendanceService;
    private final StatusMapper statusMapper;

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Create attendance", authorizations = {@Authorization(value = "apiKey")})
    public StatusDto create(@RequestBody StatusDto statusDto){
        try {
            return statusMapper.applicationStatusToStatusDto(attendanceService.create(statusMapper.statusDtoToApplicationStatus(statusDto)));
        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }


}
