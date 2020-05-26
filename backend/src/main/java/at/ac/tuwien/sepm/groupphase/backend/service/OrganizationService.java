package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface OrganizationService {
    /**
     * @return all organizations
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Organization> getAll();

    /**
     * @param id of organization to be found.
     * @return the organization with the specified id.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organization findById(int id);

    /**
     * @param organization - to be created
     * @return the created organization
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organization create(Organization organization);

    /**
     * @param organization to be updated into database with the new values.
     * @return the updated organization.
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organization update(Organization organization);

    /**
     * @param organization - to add the calendars to
     * @param calendars to be added to this organization
     * @return the updated organization
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organization addCalendars(Organization organization, Collection<Calendar> calendars);


    /**
     * @param organization - to remove the calendars from
     * @param calendars to be removed from this organization
     * @return the updated organization
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organization removeCalendars(Organization organization, Collection<Calendar> calendars);


    List<Organization> findByName(String name);

}