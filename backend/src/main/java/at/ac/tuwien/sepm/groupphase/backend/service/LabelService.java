package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.Collection;
import java.util.List;

public interface LabelService {


    /**
     * Find all labels in db.
     *
     * @return all labels
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Label> getAll() throws ServiceException;

    /**
     * Find a single label by id.
     *
     * @param id id of the label entry.
     * @return the label entry
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no label with this id is saved in database
     */
    Label findById(int id) throws ServiceException, NotFoundException;

    /**
     * Delete a single label from the db.
     *
     * @param id of the label to be deleted.
     * @throws ServiceException    if something goes wrong during data processing.
     * @throws ValidationException if the id is not present.
     */
    void delete(Integer id) throws ServiceException, ValidationException;

    /**
     * Save a new label into the db.
     *
     * @param label label to saved
     * @return the save label entry
     * @throws ServiceException    is thrown when some errors occur during the database query.
     * @throws ValidationException is thrown when the given Label does not pass validation.
     */
    Label save(Label label) throws ServiceException, ValidationException;

    /**
     * Update a single label in the db with new values
     *
     * @param label label to be updated into database with the new values.
     * @return the updated label entry.
     * @throws NotFoundException   if no event with this id is saved in db
     * @throws ServiceException    if something goes wrong during data processing.
     * @throws ValidationException if the label entity does not pass validation.
     */
    Label update(Label label) throws NotFoundException, ServiceException, ValidationException;


    /**
     * Find all labels for an event id.
     *
     * @param id id of the event entry.
     * @return the label list
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no event with this id is saved in database
     */
    List<Label> findByEventId(int id) throws ServiceException, NotFoundException;


}