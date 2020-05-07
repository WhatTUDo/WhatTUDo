package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

public interface EventService {

    void delete(Event event);

    Event save(Event event);

    Event findById(int id);
}
