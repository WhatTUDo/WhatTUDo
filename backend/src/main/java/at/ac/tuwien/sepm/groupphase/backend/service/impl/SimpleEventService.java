package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import javax.persistence.PersistenceException;
import java.util.Optional;

//TODO: annotations

@Service
public class SimpleEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final Validator validator;

    @Autowired
    public SimpleEventService(EventRepository eventRepository, Validator validator){
        this.eventRepository = eventRepository;
        this.validator = validator;
    }

    //TODO: Logging

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public Event save(Event event) {
        validator.validateNewEvent(event);
        try {
            return eventRepository.save(event);
        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Event findById(int id) {
        //TODO: logger
        Optional<Event> returned = eventRepository.findById(id);
        if(returned.isPresent()){
            return returned.get();
        }
       return null;
    }

}
