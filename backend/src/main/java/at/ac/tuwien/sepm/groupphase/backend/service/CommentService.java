package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.hibernate.service.spi.ServiceException;

import java.util.Collection;
import java.util.List;

public interface CommentService {


    /**
     * Find all comments in db.
     *
     * @return all comments
     * @throws ServiceException will be thrown if something goes wrong during data processing.
     */
    Collection<Comment> getAll() throws ServiceException;

    /**
     * Find a single comment by id.
     *
     * @param id id of the comment entry.
     * @return the comment entry
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no event with this id is saved in database
     */
    Comment findById(int id) throws ServiceException, NotFoundException;

    /**
     * Delete a single comment from the db.
     *
     * @param id of the comment to be deleted.
     * @throws ServiceException    if something goes wrong during data processing.
     * @throws ValidationException if the comment-id is not defined
     */
    void delete(Integer id) throws ServiceException, ValidationException;

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
     * @throws NotFoundException   if no event with this id is saved in db.
     * @throws ServiceException    if something goes wrong during data processing.
     * @throws ValidationException if comment entity does not pass validation.
     */
    Comment update(Comment comment) throws NotFoundException, ServiceException, ValidationException;


    /**
     * Find all comments for an event id.
     *
     * @param id id of the event entry.
     * @return the comment list.
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no event with this id is saved in database
     */
    List<Comment> findByEventId(int id) throws ServiceException, NotFoundException;

    /**
     * Find all comments for an event id.
     *
     * @param id id of the user entry.
     * @return the comment list
     * @throws ServiceException  if something goes wrong during data processing.
     * @throws NotFoundException if no event with this id is saved in database
     */
    List<Comment> findByUserId(int id) throws ServiceException, NotFoundException;


}