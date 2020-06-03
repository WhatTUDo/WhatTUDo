package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.StatusMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = AttendanceEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AttendanceEndpoint {
    static final String BASE_URL = "/attendance";
    private final AttendanceService attendanceService;
    private final StatusMapper statusMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

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

    @PreAuthorize("permitAll()")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping(value = "/getAttendees")
    @ApiOperation(value = "Create attendance", authorizations = {@Authorization(value = "apiKey")})
    public List<LoggedInUserDto> getUsersAttendingEvent(@PathVariable EventDto eventDto){
        try{
            return userMapper.applicationUserToUserDtoList(attendanceService.getUsersAttendingEvent(eventMapper.eventDtoToEvent(eventDto)));

        }
        catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

}
