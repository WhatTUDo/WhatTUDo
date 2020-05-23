package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    /**
     *
     * @param user New User with unencoded password (directly from Endpoint)
     * @return UserDetails of User.
     * @throws ServiceException is thrown if any errors occur during the database query.
     * @throws ValidationException is thrown if the User Entity does not pass validation.
     */
    ApplicationUser saveNewUser(ApplicationUser user) throws ServiceException, ValidationException;

    ApplicationUser updateUser(ApplicationUser user);

    ApplicationUser changeUserPassword(String email, String currentPassword, String newPassword);
}
