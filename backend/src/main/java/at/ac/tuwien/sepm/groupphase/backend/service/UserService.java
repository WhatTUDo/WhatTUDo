package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    /**
     * Save new user into db.
     *
     * @param user New User with unencoded password (directly from Endpoint)
     * @return UserDetails of User
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the User Entity does not pass validation.
     */
    ApplicationUser saveNewUser(ApplicationUser user) throws ServiceException, ValidationException;

    /**
     * Update a existing user in the db with new values.
     *
     * @param user user to be updated in db with new values
     * @return UserDetails of User
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the User Entity does not pass validation.
     */
    ApplicationUser updateUser(ApplicationUser user) throws ServiceException, ValidationException;

    /**
     * Change the password of an existing user.
     *
     * @param email           email of user who's password will be changed
     * @param currentPassword current Password of user
     * @param newPassword     new Password of user
     * @return UserDetails of User
     * @throws ValidationException is thrown if the email/currentPassword/newPassword do not pass validation.
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws NotFoundException   is thrown if no user with specified email can be found in db.
     */
    ApplicationUser changeUserPassword(String email, String currentPassword, String newPassword) throws ServiceException, ValidationException, NotFoundException;

    ApplicationUser updateRoleInOrga(ApplicationUser user, Organization organization, OrganizationRole organizationRole);

    ApplicationUser removeFromOrga(ApplicationUser user, Organization organization);
}
