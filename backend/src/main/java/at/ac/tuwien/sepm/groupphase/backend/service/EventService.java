package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

public interface EventService {

    void delete(Event event);

    /**
     * @param event to be saved into database .
     * @return the new event.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if.
     *         - name of event is empty;
     *         - startDateTime is after endDateTime;
     */
    Event save(Event event);

    /**
     * @param id of event to be found.
     * @return the event with the specified id.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Event findById(int id);


    /**
     * @param event to be updated into database with the new values.
     * @return the new event.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if.
     *
     *         - startDateTime is after endDateTime;
     */
    Event update(Event event);

}