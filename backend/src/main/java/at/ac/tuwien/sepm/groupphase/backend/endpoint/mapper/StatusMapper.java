package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatusDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.AttendanceStatus;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Mapper(componentModel = "spring")
public abstract class StatusMapper {
    @Autowired
    protected AttendanceRepository attendanceRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventRepository eventRepository;

    @BeforeMapping
    protected void mapEvent(StatusDto statusDto, @MappingTarget AttendanceStatus applicationStatus){
       Optional<Event> found= eventRepository.findById(statusDto.getEventId());
       if(!found.isPresent()){
           throw new NotFoundException("Event not found");
       }
        applicationStatus.setEvent(found.get());
    }

    @BeforeMapping
    protected void mapUser(StatusDto statusDto, @MappingTarget AttendanceStatus applicationStatus){
        Optional<ApplicationUser> found = userRepository.findById(statusDto.getUserId());
        if(!found.isPresent()){
            throw new NotFoundException("User not found");
        }
        applicationStatus.setUser(found.get());
    }

    public abstract AttendanceStatus statusDtoToApplicationStatus(StatusDto statusDto);

    @BeforeMapping
    protected void mapEvent(AttendanceStatus applicationStatus, @MappingTarget StatusDto statusDto){
       statusDto.setEventId(applicationStatus.getEvent().getId());
    }

    @BeforeMapping
    protected void mapUser(AttendanceStatus applicationStatus, @MappingTarget StatusDto statusDto){
       statusDto.setUserId(applicationStatus.getUser().getId());
    }

    public abstract StatusDto applicationStatusToStatusDto(AttendanceStatus applicationStatus);

}
