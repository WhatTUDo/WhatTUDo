package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

//TODO: annotations

public interface EventService {

    Event save(Event event);

    Event findById(int id);
}
