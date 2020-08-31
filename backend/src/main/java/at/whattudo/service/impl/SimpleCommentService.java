package at.whattudo.service.impl;


import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Comment;
import at.whattudo.entity.Event;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.CommentRepository;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.UserRepository;
import at.whattudo.service.CommentService;
import at.whattudo.util.ValidationException;
import at.whattudo.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleCommentService implements CommentService {
    private final ApplicationEventPublisher publisher;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final Validator validator;



    @Override
    public Collection<Comment> getAll() {
        try {
            return commentRepository.findAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }



    @Override
    public Comment findById(int id) {
        try {
            Optional<Comment> found = commentRepository.findById(id);
            if (found.isPresent()) {
                Comment comment = found.get();

                return comment;
            } else {
                throw new NotFoundException("No comment found with id " + id);
            }
        }
        catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }



    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            if (id != null) {
                this.findById(id);
            } else {
                throw new ValidationException("Id is not defined");
            }

            Comment toDelete = this.findById(id);
            Event event = toDelete.getEvent();

            try {
                //   event.getComments().remove(toDelete);
                eventRepository.save(event);

                commentRepository.delete(toDelete);

            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage(), e);
            }

        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Comment save(Comment comment) {
        try {


            Comment result = comment;

            comment.getEvent();
            // toSetComment.getComments().add(result);

            result = commentRepository.save(result);

            return result;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Comment update(Comment comment) {
        try {

            Comment toUpdate = this.findById(comment.getId());

            if (!(comment.getText().isBlank()) || !(comment.getText().equals((this.findById(comment.getId())).getText()))) {
                toUpdate.setText(comment.getText());
            }

            toUpdate.setUser(this.findById(comment.getId()).getUser());
            toUpdate.setEvent(this.findById(comment.getId()).getEvent());

            //    publisher.publishEvent(new EventUpdateEvent(comment.getId()));
            return commentRepository.save(toUpdate);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Comment> findByEventId(int id) {

        List<Comment> result = new ArrayList<Comment>();

        Optional<Event> found = eventRepository.findById(id);

        if (found.isPresent()) {
            Event finditscomment = found.get();


            commentRepository.findAll().forEach(it -> {
                if ((it.getEvent().equals(finditscomment)))
                    result.add(it);
            });
        } else {
            throw new NotFoundException();
        }

        return result;

    }

    public List<Comment> findByUserId(int id) {

        List<Comment> result = new ArrayList<Comment>();

        Optional<ApplicationUser> found = userRepository.findById(id);

        if (found.isPresent()) {
            ApplicationUser finditscomment = found.get();


            commentRepository.findAll().forEach(it -> {
                if ((it.getUser().equals(finditscomment)))
                    result.add(it);
            });
        } else {
            throw new NotFoundException();
        }

        return result;

    }

}
