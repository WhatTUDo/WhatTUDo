package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.criterion.Example;
import org.hibernate.service.spi.ServiceException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CommentService {


    /**
     * Find all comments in db.
     *
     * @return all comments
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Comment> getAll();

    /**
     * Find a single comment by id.
     *
     * @param id id of the comment entry.
     * @return the comment entry
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    Comment findById(int id);

    /**
     * Delete a single comment from the db.
     *
     * @param id of the comment to be deleted.
     * @throws org.hibernate.service.spi.ServiceException                    if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException if:
     *                                                                       - id == null;
     *                                                                       - TODO IA/IDA
     */
    void delete(Integer id);

    /**
     * Save a new comment into the db.
     *
     * @param comment comment to saved
     * @return the saved comment entry
     * @throws ServiceException    is thrown when some errors occur during the database query.
     * @throws ValidationException is thrown when the given Label does not pass validation.
     */
    Comment save(Comment comment) throws ServiceException, ValidationException;

    /**
     * Update a single comment in the db with new values
     *
     * @param comment comment to be updated into database with the new values.
     * @return the updated comment entry.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in db
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     *                                                                          - TODO IllegalArgument + IvalidaDataAccess
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException    if:
     *                                                                          - id is empty
     */
    Comment update(Comment comment);


    /**
     * Find all comments for an event id.
     *
     * @param id id of the event entry.
     * @return the comment list
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    List<Comment> findByEventId(int id);

    /**
     * Find all comments for an event id.
     *
     * @param id id of the user entry.
     * @return the comment list
     * @throws org.hibernate.service.spi.ServiceException                       if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if no event with this id is saved in database
     */
    List<Comment> findByUserId(int id);


}