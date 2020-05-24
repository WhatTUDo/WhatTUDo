package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
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
    private final OrganizationRepository organizationRepository;
    private final OrganizationService organizationService;

    @Override
    public Calendar findById(Integer id) throws NotFoundException, ServiceException {
        try {
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
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Calendar> findByName(String name) throws ServiceException {
        try {
            List<Calendar> calendars = calendarRepository.findAllByNameContains(name);
            return calendars;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public List<Calendar> findAll() {
        try {

            List<Calendar> result = new ArrayList<>();

            for (Calendar c : calendarRepository.findAll()) {
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
            Calendar result = calendarRepository.save(calendar);
            if (calendar.getOrganizations() == null) {
                throw new ServiceException("Could not find Organizations for Calendar");
            }
            for (Organization o : calendar.getOrganizations()) {
                List<Calendar> cal;
                cal = o.getCalendars();
                cal.add(calendar);
                o.setCalendars(cal);
                organizationRepository.save(o);
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
            List<Organization> olist = toDelete.getOrganizations();

            try {
                olist.forEach(it -> it.getCalendars().remove(toDelete));
                organizationRepository.saveAll(olist);

                toDelete.getOrganizations().removeAll(olist);

                if(this.findById(id).getEvents() != null){

                    for(Event e : toDelete.getEvents()){

                        eventRepository.delete(e);
                    }

                    List<Event> empty = new ArrayList<Event>();
                    toDelete.setEvents(empty);

                }

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

    @Override
    public Calendar update(Calendar calendar) {
        try {

            Calendar toUpdate = this.findById(calendar.getId());

            if(!(calendar.getName().isBlank()) || !(calendar.getName().equals((this.findById(calendar.getId())).getName()))){
               toUpdate.setName(calendar.getName());
                }

            toUpdate.setOrganizations(calendar.getOrganizations());


            /**   List<Calendar> thiscal = new ArrayList<Calendar>();
                thiscal.add(calendar);
                 for(Organisation o : calendar.getOrganisations()){

                 List<Calendar> cal;
                 cal = o.getCalendars();
                 if(!(cal.contains(calendar))) {
                     cal.add(calendar);
                 }
                 organisationService.addCalendars(o,thiscal);

                 }**/

              //  Calendar result =  calendarRepository.save(this.findById(calendar.getId()));

            publisher.publishEvent(new EventCreateEvent(calendar.getName()));
            return calendarRepository.save(toUpdate);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
