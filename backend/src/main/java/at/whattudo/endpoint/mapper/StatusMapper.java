package at.whattudo.endpoint.mapper;

import at.whattudo.endpoint.dto.StatusDto;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.AttendanceStatus;
import at.whattudo.entity.Event;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.AttendanceRepository;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.UserRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class StatusMapper {
    @Autowired
    protected AttendanceRepository attendanceRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventRepository eventRepository;

    @BeforeMapping
    protected void mapEvent(StatusDto statusDto, @MappingTarget AttendanceStatus applicationStatus) {
        Optional<Event> found = eventRepository.findById(statusDto.getEventId());
        if (!found.isPresent()) {
            throw new NotFoundException("Event not found");
        }
        applicationStatus.setEvent(found.get());
    }

    @BeforeMapping
    protected void mapUser(StatusDto statusDto, @MappingTarget AttendanceStatus applicationStatus) {
        Optional<ApplicationUser> found = userRepository.findByName(statusDto.getUsername());
        if (!found.isPresent()) {
            throw new NotFoundException("User not found");
        }
        applicationStatus.setUser(found.get());
    }

    @BeforeMapping
    protected void mapStatus(@MappingTarget AttendanceStatus attendanceStatus) {
        attendanceStatus.setLastModified(LocalDateTime.now());
    }

    public abstract AttendanceStatus statusDtoToApplicationStatus(StatusDto statusDto);

    @BeforeMapping
    protected void mapEvent(AttendanceStatus applicationStatus, @MappingTarget StatusDto statusDto) {
        statusDto.setEventId(applicationStatus.getEvent().getId());
    }

    @BeforeMapping
    protected void mapUser(AttendanceStatus applicationStatus, @MappingTarget StatusDto statusDto) {
        statusDto.setUsername(applicationStatus.getUser().getUsername());
    }


    public abstract StatusDto applicationStatusToStatusDto(AttendanceStatus applicationStatus);

}
