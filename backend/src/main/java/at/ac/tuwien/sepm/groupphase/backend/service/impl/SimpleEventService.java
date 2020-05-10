package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventFindEvent;
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

import javax.persistence.PersistenceException;
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
            eventRepository.findById(event.getId());
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
            publisher.publishEvent(new EventCreateEvent(event.getName()));
            return eventRepository.save(event);
        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event findById(int id) {
        Optional<Event> found = eventRepository.findById(id);
        if (found.isPresent()) {
            Event event = found.get();
            publisher.publishEvent(new EventFindEvent(event.getName()));
            return event;
        } else {
            throw new NotFoundException("No event found with id " + id);
        }
    }
}
