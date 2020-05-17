package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

public interface OrganisationService {
    /**
     * @return all organisations
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Organisation> getAll();

    /**
     * @param id of organisation to be found.
     * @return the organisation with the specified id.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organisation findById(int id);

    /**
     * @param organisation - to be created
     * @return the created organisation
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organisation create(Organisation organisation);

    /**
     * @param organisation to be updated into database with the new values.
     * @return the updated organisation.
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organisation update(Organisation organisation);

    /**
     * @param organisation - to add the calendars to
     * @param calendars to be added to this organisation
     * @return the updated organisation
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organisation addCalendars(Organisation organisation, Collection<Calendar> calendars);


    /**
     * @param organisation - to remove the calendars from
     * @param calendars to be removed from this organisation
     * @return the updated organisation
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Organisation removeCalendars(Organisation organisation, Collection<Calendar> calendars);


    List<Organisation> findByName(String name);

}