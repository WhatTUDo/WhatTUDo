package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired protected UserRepository userRepository;


    public abstract UserDto applicationUserToUserDto(ApplicationUser user);
    public abstract ApplicationUser userDtoToApplicationUser(UserDto userDto);
}
