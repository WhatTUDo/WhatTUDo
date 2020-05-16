package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
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
    private final OrganisationRepository organisationRepository;

    @Override
    public Calendar findById(Integer id) {
        Optional<Calendar> found = calendarRepository.findById(id);
        if (found.isPresent()) {

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

            Calendar calendar = (found.get());
            calendar.setEvents(events);

            List<Organisation> organisations = new ArrayList<Organisation>();
           // calendar.setOrganisations(organisations);
            publisher.publishEvent(new CalendarFindEvent(calendar.getName()));
            return calendar;
        } else {
            throw new NotFoundException("No event found with id " + id);
        }
    }

    @Override
    public List<Calendar> findAll() {
        try {

            List<Calendar> result = new ArrayList<Calendar>();

             for(Calendar c : calendarRepository.findAll()){
                 result.add(findById(c.getId()));
             }
             return result;

        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public Calendar save(Calendar calendar) {
        try {

          Calendar result =  calendarRepository.save(calendar);
           for(Organisation o : calendar.getOrganisations()){

              List<Calendar> cal = new ArrayList<Calendar>();
                cal = o.getCalendars();
                cal.add(calendar);
                o.setCalendars(cal);
                organisationRepository.save(o);

            }
            return result;
        } catch (PersistenceException e) { //TODO: insert right exception
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
