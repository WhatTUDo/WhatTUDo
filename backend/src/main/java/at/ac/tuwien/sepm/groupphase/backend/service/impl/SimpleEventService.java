package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        eventRepository.delete(event);
    }

    @Override
    //TODO: try,catch, logger
    public Event save(Event event) {
        return eventRepository.save(event);
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
