package at.whattudo.endpoint.mapper;

import at.whattudo.endpoint.dto.IncomingUserDto;
import at.whattudo.endpoint.dto.LoggedInUserDto;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected UserRepository userRepository;


    public abstract LoggedInUserDto applicationUserToUserDto(ApplicationUser user);

    public abstract ApplicationUser userDtoToApplicationUser(IncomingUserDto userDto);

    public abstract ApplicationUser loggedInUserDtoToApplicationUser(LoggedInUserDto userDto);

    public abstract List<LoggedInUserDto> applicationUserToUserDtoList(List<ApplicationUser> applicationUsers);
}
