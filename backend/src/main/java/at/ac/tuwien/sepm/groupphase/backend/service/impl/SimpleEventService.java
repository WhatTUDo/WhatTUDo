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
import javax.validation.constraints.Null;
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
          eventRepository.findById(event.getId());
        // update calendar by removing Event from List of Events
//            List<Event> newList = new ArrayList(Arrays.asList((event.getCalendar()).getEvents()));
//            newList.remove(event);
//
//            (event.getCalendar()).setEvents(newList);
//            calendarRepository.update(event.getCalendar());

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

    @Override
    public Event update(Event event) {
        LOGGER.info("Service update event {}", event);
        try {


            Event tochange = eventRepository.findById(event.getId()).get();
            //TODO check with isPresent first
            if(!(event.getName().isBlank())) {
                tochange.setName(event.getName());
            }

         /**   if (!(event.getName().isBlank()) && !(event.getName().isEmpty())) {

                tochange.setName(event.getName());
            }

            if (event.getEndDateTime().compareTo(event.getStartDateTime()) < 0) {

                throw new ValidationException("end-date must be after start-date");
            }

            //TODO: better validation
          **/

            if((event.getStartDateTime() != null) && (event.getStartDateTime().isBefore(event.getEndDateTime()))){
                tochange.setStartDateTime(event.getStartDateTime());
            }
            if((event.getEndDateTime() != null)&& (event.getStartDateTime().isBefore(event.getEndDateTime()))){
                tochange.setEndDateTime(event.getEndDateTime());
            }

            if((event.getStartDateTime().getYear() < 2020)){
                throw new ValidationException("start date not valid");
            }

            if(!(event.getStartDateTime().isBefore(event.getEndDateTime()))){
                throw new ValidationException("start date can't be before end date!");
            }

            tochange.setCalendar(event.getCalendar());

           return eventRepository.save(tochange);

        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e){
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e){
            throw new ServiceException(e.getMessage(),e);
        }
    }


}
