package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import javax.persistence.PersistenceException;
import java.util.*;

//TODO: annotations

@Service
public class SimpleEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final Validator validator;

    @Autowired
    public SimpleEventService(EventRepository eventRepository, Validator validator,CalendarRepository calendarRepository){
        this.eventRepository = eventRepository;
        this.validator = validator;
        this.calendarRepository=calendarRepository;
    }


    @Override
    public void delete(Event event) {
        LOGGER.info("Service delete {}", event);
        try{
            if(event.getId() != null){
          this.findById(event.getId());}
            else{
                throw new ValidationException("Id is not defined");
            }

        eventRepository.delete(event);
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e){
           throw new ValidationException(e.getMessage());
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override
    public Event save(Event event) {

        LOGGER.trace("save({})", event.getName());
        validator.validateNewEvent(event);
        try {
            return eventRepository.save(event);
        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event findById(int id) {
        LOGGER.trace("searching for event with id: " + id);
        Optional<Event> returned = eventRepository.findById(id);
        if(returned.isPresent()) {
            return returned.get();
        } else {
            throw new NotFoundException("No event found with id " +id);
        }
    }
}
