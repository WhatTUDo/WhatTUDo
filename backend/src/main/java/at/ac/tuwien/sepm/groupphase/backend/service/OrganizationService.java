package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface OrganizationService {

    /**
     * Find all organizations in db.
     *
     * @return all organizations
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Organization> getAll();

    /**
     * Find a single organization by id.
     *
     * @param id id of organization to be found.
     * @return the organization entry with the specified id.
     * @throws org.hibernate.service.spi.ServiceException                       will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no organization with id is in db.
     */
    Organization findById(int id);

    /**
     * Create new organization and save into db.
     *
     * @param organization - to be created and saved into db.
     * @return the created organization
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - name is blank;
     *                                                                       - an organization with this id already exists
     *                                                                       - an organization with this name already exists
     */
    Organization create(Organization organization);

    /**
     * Update a saved organization with new values
     *
     * @param organization to be updated into database with the new values.
     * @return the updated organization.
     * @throws org.hibernate.service.spi.ServiceException                       will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException    will be thrown if name is blank.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no organization with this id is save in db.
     */
    Organization update(Organization organization);

    /**
     * Add calendars to a organization.
     *
     * @param organization - to add the calendars to
     * @param calendars    to be added to this organization
     * @return the updated organization
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organization addCalendars(Organization organization, Collection<Calendar> calendars);

    /**
     * Remove calendars from an organization.
     *
     * @param organization - to remove the calendars from
     * @param calendars    to be removed from this organization
     * @return the updated organization
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organization removeCalendars(Organization organization, Collection<Calendar> calendars);

    /**
     * Find organizations by name
     *
     * @param name name (or part of a name) for which a search will be performed.
     * @return List of organization entities that match the search-name
     * @throws org.hibernate.service.spi.ServiceException if something goes wrong during data processing.
     */
    List<Organization> findByName(String name);

}