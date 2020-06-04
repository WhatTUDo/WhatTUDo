package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IncomingUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LoggedInUserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired protected UserRepository userRepository;


    public abstract LoggedInUserDto applicationUserToUserDto(ApplicationUser user);
    public abstract ApplicationUser userDtoToApplicationUser(IncomingUserDto userDto);
    public abstract ApplicationUser loggedInUserDtoToApplicationUser(LoggedInUserDto userDto);

    public abstract List<LoggedInUserDto> applicationUserToUserDtoList(List<ApplicationUser> applicationUsers);
}
