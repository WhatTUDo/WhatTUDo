package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class SimpleCalendarService implements CalendarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CalendarRepository calendarRepository;

    @Autowired
    public SimpleCalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }


    @Override
    public Calendar findById(Integer id) {
        LOGGER.trace("searching for event with id: " + id);
        Optional<Calendar> returned = calendarRepository.findById(id);
        if(returned.isPresent()) {
            return returned.get();
        } else {
            throw new NotFoundException("No calendar found with id " +id);
        }
    }
}
