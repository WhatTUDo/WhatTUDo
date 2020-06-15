package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventUpdateEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleEventService implements EventService {
    private final ApplicationEventPublisher publisher;
    private final EventRepository eventRepository;
    private final LabelRepository labelRepository;
    private final AttendanceRepository attendanceRepository;
    private final Validator validator;


    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            if (id <= 0) {
                throw new ValidationException("Id is not valid");
            }
            Event toDelete = this.findById(id);
            if (toDelete.getLabels() != null && toDelete.getLabels().size() > 0) {
                removeLabels(toDelete, toDelete.getLabels());
            }
            if (toDelete.getAttendanceStatuses() != null && !toDelete.getAttendanceStatuses().isEmpty()) {
                attendanceRepository.deleteAll(toDelete.getAttendanceStatuses());
            }
            eventRepository.deleteById(id);
            publisher.publishEvent(new EventDeleteEvent(toDelete.getName()));
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Event> findForDates(LocalDateTime start, LocalDateTime end) {
        try {
            validator.validateMultipleEventsQuery(start, end);
            List<Event> events = new ArrayList<>();
            events = eventRepository.findAllByStartDateTimeBetween(start, end);
            return events;
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

    @Override
    public Event addLabels(Event event, Collection<Label> labels) {
        log.info("Adding labels {} to event {}", labels, event);
        try {
            labels.forEach(it -> {
                if (!(it.getEvents().contains(event))) {
                    it.getEvents().add(event);
                }
            });
            labelRepository.saveAll(labels);
            if (event.getLabels() != null) {
                event.getLabels().addAll(labels);
            } else {
                List<Label> labelList = new ArrayList<>(labels);
                event.setLabels(labelList);
            }
            return eventRepository.save(event);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event updateLabels(Event event, Collection<Label> labels) {
        log.info("Adding labels {} to event {}", labels, event);
        try {
            removeLabels(event, event.getLabels());
            for (Label l : labels
            ) {
                if (!l.getEvents().contains(event)) {
                    List<Event> events = new ArrayList<>(l.getEvents());
                    events.add(event);
                    l.setEvents(events);
                }
            }
            //labels.forEach(it -> {if (!(it.getEvents().contains(event))){it.getEvents().add(event);}});
            labelRepository.saveAll(labels);
            if (event.getLabels() != null) {
                event.getLabels().addAll(labels);
            } else {
                List<Label> labelList = new ArrayList<>(labels);
                event.setLabels(labelList);
            }
            return eventRepository.save(event);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public Event removeLabels(Event event, Collection<Label> labels) {
        //   log.info("Removing labels {} from event {}", labels, event);
        try {
            if (labels != null) {
                labels.forEach(it -> {
                    it.getEvents().remove(event);
                });
                labelRepository.saveAll(labels);
                event.getLabels().removeAll(labels);
            }
            return eventRepository.save(event);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


    public List<Event> getByCalendarId(Integer id) throws ServiceException {
        try {
            return eventRepository.findByCalendarId(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

}
