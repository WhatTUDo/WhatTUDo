package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.criterion.Example;
import org.hibernate.service.spi.ServiceException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface LabelService {



    /**
     * Find all labels in db.
     *
     * @return all labels
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Label> getAll();

    /**
     * Find a single event by id.
     *
     * @param id id of the event entry.
     * @return the event entry
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    Label findById(int id);

    /**
     * Delete a single calendar from the db.
     *
     * @param id of the label to be deleted.
     * @throws org.hibernate.service.spi.ServiceException                    if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - id == null;
     *                                                                       - TODO IA/IDA
     */
    void delete(Integer id);

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
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in db
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     *                                                                          - TODO IllegalArgument + IvalidaDataAccess
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException    if:
     *                                                                          - name is empty
     */
    Label update(Label label);


    /**
     * Find all labels for an event id.
     *
     * @param id id of the event entry.
     * @return the label list
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    List<Label> findByEventId(int id);


}