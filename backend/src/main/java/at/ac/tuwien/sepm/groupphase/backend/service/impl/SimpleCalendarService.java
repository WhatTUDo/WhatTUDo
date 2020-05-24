package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
    private final OrganisationService organisationService;


    @Override
    public Calendar findById(Integer id) {
        Optional<Calendar> found = calendarRepository.findById(id);
        if (found.isPresent()) {

            List<Event> events;

            events = eventRepository.findByCalendarId(id);


            Calendar calendar = (found.get());
            calendar.setEvents(events);

            return calendar;
        } else {
            throw new NotFoundException("No event found with id " + id);
        }
    }

    @Override
    public List<Calendar> findByName(String name){
        try{

            return calendarRepository.findAllByNameContains(name);
        }
        catch (PersistenceException e){
            throw new ServiceException(e.getMessage());
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

        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public Calendar save(Calendar calendar) {
        try {

          Calendar result =  calendarRepository.save(calendar);
           for(Organisation o : calendar.getOrganisations()){

              List<Calendar> cal;
                cal = o.getCalendars();
                cal.add(calendar);
                o.setCalendars(cal);
                organisationRepository.save(o);

            }

            publisher.publishEvent(new EventCreateEvent(calendar.getName()));
            return result;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            if (id != null) {
                this.findById(id);
            } else {
                throw new ValidationException("Id is not defined");
            }
            publisher.publishEvent(new EventDeleteEvent(this.findById(id).getName()));

            Calendar toDelete = this.findById(id);
            List<Organisation> olist = toDelete.getOrganisations();

            try {
                olist.forEach(it -> it.getCalendars().remove(toDelete));
                organisationRepository.saveAll(olist);

                toDelete.getOrganisations().removeAll(olist);
                calendarRepository.delete(toDelete);

            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage(), e);
            }

        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
