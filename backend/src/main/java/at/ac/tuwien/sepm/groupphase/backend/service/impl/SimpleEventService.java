package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.parser.Entity;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class SimpleEventService implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @Autowired
    public SimpleEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public void delete(Event event) {
        LOGGER.info("Service delete {}", event);
        try{
        //(event.getCalendar()).getEvents().remove(event); not allowed ?
        eventRepository.delete(event);
        } catch (IllegalArgumentException e){
            //handle exception.
        }
    }

    @Override
    //TODO: try,catch, logger
    public Event save(Event event) {
        LOGGER.info("Service save {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event findById(int id) {
//        LOGGER.info("Service findById: {}", id);
//        Optional<Event> returned = eventRepository.findById(id);
//        if(returned.isPresent()){
//            return returned.get();
//        }
//       return null;
//    }
        try {
            Optional<Event> returned = eventRepository.findById(id);
            if(returned.isPresent()){
            return returned.get();
        }else return null;
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Event with id:"+ id + " does not exist");
        }
    }

}
