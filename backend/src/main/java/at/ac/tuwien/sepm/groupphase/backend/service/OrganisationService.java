package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;

public interface OrganisationService {

    /**
     * @param organisation to be updated into database with the new values.
     * @return the updated organisation.
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organisation update(Organisation organisation);

    /**
     * @param organisation - to be created
     * @return the created organisation
     * @throws org.hibernate.service.spi.ServiceException                    will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if name is blank.
     */
    Organisation create(Organisation organisation);
}