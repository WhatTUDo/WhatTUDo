package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    /**
     * Gets all users
     *
     * @return list of all users
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<ApplicationUser> getAllUsers() throws ServiceException;

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

    //TODO:

    /**
     * @param user
     * @param organization
     * @param organizationRole
     * @return
     */
    ApplicationUser updateRoleInOrga(ApplicationUser user, Organization organization, OrganizationRole organizationRole);

    //TODO:

    /**
     * @param user
     * @param organization
     * @return
     */
    ApplicationUser removeFromOrga(ApplicationUser user, Organization organization);

    /**
     * find user by id.
     *
     * @param id of user that is being searched.
     * @return found user
     * @throws NotFoundException is thrown when user is not found.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     */
    ApplicationUser findUserById(Integer id) throws NotFoundException, ServiceException;

    /**
     * Search user by name
     *
     * @param name of user who user is looking for
     * @return found user
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    ApplicationUser getUserByName(String name) throws ServiceException;

    /**
     * Get list of organization that user has a membership at.
     *
     * @param userId id of user.
     * @return list of organizations where user has a membership
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Organization> getUserOrganizations(Integer userId) throws ServiceException;

    /**
     * gets a List of event-recommendations  for a user based on labels of
     * previous events the user has attended or was interested in
     *
     * @param userId id of the loggedIn user
     * @return List of 4 recommended Events if a recommendation can be made, otherwise a List of recommended/random events
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Set<Event> getRecommendedEvents(Integer userId) throws ServiceException;
}
