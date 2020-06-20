package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.Collection;

public interface LocationService {

    /**
     * Find all labels in db.
     *
     * @return all locations
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Location> getAll() throws ServiceException;

    /**
     * Find a single location by id.
     *
     * @param id of the location entry.
     * @return the location entry
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no event with this id is saved in database
     */
    Location findById(int id);

     /**
     * Save a new location into the db.
     *
     * @param location to saved
     * @return the saved location entry
     * @throws ServiceException    is thrown when some errors occur during the database query.
     * @throws ValidationException is thrown when the given Location does not pass validation.
     */
    Location save(Location location) throws ServiceException, ValidationException;

    /**
     * Update a single location in the db with new values
     *
     * @param location to be updated into database with the new values.
     * @return the updated location entry.
     * @throws NotFoundException   if no event with this id is saved in db
     * @throws ServiceException    if something goes wrong during data processing.
     * @throws ValidationException is thrown when the given Location does not pass validation.
     */
   Location update(Location location) throws ServiceException,ValidationException,NotFoundException;

    /**
     * Delete a single location from the db.
     *
     * @param id of the location to be deleted.
     * @throws ServiceException if something goes wrong during data processing.
     */
    void delete(Integer id);


}
