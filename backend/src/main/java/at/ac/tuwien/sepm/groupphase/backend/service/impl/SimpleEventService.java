package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventUpdateEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleEventService implements EventService {
    private final ApplicationEventPublisher publisher;
    private final EventRepository eventRepository;
    private final Validator validator;


    @Override
    public void delete(Event event) {
        try {
            if (event.getId() != null) {
                this.findById(event.getId());
            } else {
                throw new ValidationException("Id is not defined");
            }
            eventRepository.delete(event);
            publisher.publishEvent(new EventDeleteEvent(event.getName()));
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event save(Event event) {
        validator.validateNewEvent(event);
        try {
            Event createdEvent = eventRepository.save(event);
            publisher.publishEvent(new EventCreateEvent(createdEvent.getName()));
            return createdEvent;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    //FIXME: add NotFoundException wenn leer zurückkommt?
    @Override
    public List<Event> findByName(String name) {
        try {
            return eventRepository.findAllByNameContains(name);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Event findById(int id) {
        try {
            Optional<Event> found = eventRepository.findById(id);
            if (found.isPresent()) {
                return found.get();
            } else {
                throw new NotFoundException("No event found with id " + id);
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Event> findForDates(LocalDateTime start, LocalDateTime end) {
        try {
            validator.validateMultipleEventsQuery(null, start, end);
            List<Event> foundEvents = eventRepository.findAllByStartDateTimeBetween(start, end);
            if (foundEvents.size() == 0) {
                throw new NotFoundException("Could not find any events between the specified dates: " + start.toString() + ", " + end.toString());
            }
            return foundEvents;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event update(Event event) {
        try {
            Optional<Event> found = eventRepository.findById(event.getId());
            if (found.isPresent()) {
                validator.validateUpdateEvent(event);
                Event savedEvent = eventRepository.save(event);
                publisher.publishEvent(new EventUpdateEvent(savedEvent.getName()));
                return savedEvent;
            } else {
                throw new NotFoundException("No event found with id " + event.getId());
            }
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


}
