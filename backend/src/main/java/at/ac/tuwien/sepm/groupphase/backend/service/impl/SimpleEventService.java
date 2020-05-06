package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//TODO: annotations

@Service
public class SimpleEventService implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public SimpleEventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
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
