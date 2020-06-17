package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import org.hibernate.service.spi.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;

import java.util.List;
import java.util.Collection;

public interface OrganizationService {

    /**
     * Find all organizations in db.
     *
     * @return all organizations
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Collection<Organization> getAll() throws ServiceException;

    /**
     * Find a single organization by id.
     *
     * @param id id of organization to be found.
     * @return the organization entry with the specified id.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException is thrown if no organization with id can be found in db.
     */
    Organization findById(int id) throws ServiceException, NotFoundException;

    /**
     * Create new organization and save into db.
     *
     * @param organization organization to be saved into db.
     * @return the created organization
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Organization Entity does not pass validation.
     */
    Organization create(Organization organization) throws ServiceException, ValidationException;

    /**
     * Update a saved organization with new values
     *
     * @param organization to be updated into database with the new values.
     * @return the updated organization.
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     * @throws ValidationException is thrown if the Organization Entity does not pass validation.
     * @throws NotFoundException   is thrown if no organization can be found in db.
     */
    Organization update(Organization organization) throws ServiceException, ValidationException, NotFoundException;

    /**
     * Deletes the Organization with the specified id. Also removes all Calendars associated with the Organization.
     *
     * @param organisationID ID of the Organization to delete
     * @return ID of the deleted Organization.
     * @throws ServiceException  is thrown if something goes wrong during data processing.
     * @throws NotFoundException is thrown if no organization can be found in db.
     */
    Integer delete(Integer organisationID) throws ServiceException, NotFoundException;

    /**
     * Add calendars to a organization.
     *
     * @param organization - to add the calendars to
     * @param calendars    to be added to this organization
     * @return the updated organization
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Organization addCalendars(Organization organization, Collection<Calendar> calendars) throws ServiceException;

    /**
     * Remove calendars from an organization.
     *
     * @param organization - to remove the calendars from
     * @param calendars    to be removed from this organization
     * @return the updated organization
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Organization removeCalendars(Organization organization, Collection<Calendar> calendars) throws ServiceException;

    /**
     * Find organizations by name
     *
     * @param name name (or part of a name) for which a search will be performed.
     * @return List of organization entities that match the search-name
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<Organization> findByName(String name) throws ServiceException;

    /**
     * Find members of organization with id id.
     *
     * @param id id of organization whose members are returned
     * @return list of members of organization with id id.
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    List<ApplicationUser> getMembers(Integer id) throws ServiceException;

    /**
     * add member/moderator to organization
     * @param user to add to org
     * @param organization org to update user
     * @param role role user will have in org
     * @throws ServiceException is thrown if something goes wrong during data processing.
     */
    Organization addMembership(ApplicationUser user, Organization organization, String role) throws ServiceException;

    /**
     * Sets the cover image
     * @param organization to set the cover to
     * @param imageBlob - image blob
     * @return the changed calendar
     * @throws ServiceException    is thrown if something goes wrong during data processing.
     */
    Organization setCoverImage(Organization organization, byte[] imageBlob);
}