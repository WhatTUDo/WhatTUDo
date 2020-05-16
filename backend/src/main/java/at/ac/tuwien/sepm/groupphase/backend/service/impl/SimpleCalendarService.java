package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleCalendarService implements CalendarService {
    private final ApplicationEventPublisher publisher;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    @Override
    public Calendar findById(Integer id) {
        Optional<Calendar> found = calendarRepository.findById(id);
        System.out.println("found one");
        if (found.isPresent()) {
            System.out.println(found.get().getId() + " " + found.get().getName());

            List<Event> events = new ArrayList<Event>();
            List<Integer> eventIds = new ArrayList<Integer>();

            events = eventRepository.findByCalendarId(id);


            for(Event e : events){
                eventIds.add(e.getId());
            }


            /**List<Event> events = new ArrayList<Event>();

            Optional<Event> foundevent = eventRepository.findById(3);
            if (foundevent.isPresent()) {
                Event event = foundevent.get();
                events.add(event);
            } else {
                throw new NotFoundException("No event found with id " + id);
            }
            Optional<Event> found2event = eventRepository.findById(4);
            if (found2event.isPresent()) {
                Event event = found2event.get();
                events.add(event);
            } else {
                throw new NotFoundException("No event found with id " + id);
            } **/


            System.out.println("Events: " + events);
            System.out.println(eventRepository.findByCalendarId(id).toString() + "hier");

            System.out.println("Orgas:" + found.get());
            Calendar calendar = (found.get());
            calendar.setEvents(events);
            publisher.publishEvent(new CalendarFindEvent(calendar.getName()));
            return calendar;
        } else {
            throw new NotFoundException("No event found with id " + id);
        }
    }

    @Override
    public Calendar save(Calendar calendar) {
        try {

                //TODO: save relations in link table (orga- cal) too! And then change other methods
            return calendarRepository.save(calendar);
        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
