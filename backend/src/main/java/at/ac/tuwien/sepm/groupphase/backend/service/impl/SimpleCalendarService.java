package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
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
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
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
    private final EventService eventService;
    private final EventMapper eventMapper;

    private final EventEndpoint eventEndpoint;
    private final Validator validator;


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
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Calendar> findByName(String name) throws ServiceException {
        try {
            return calendarRepository.findAllByNameContainingIgnoreCase(name);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    //FIXME: return calendarRepository.findAll
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
            validator.validateNewCalendar(calendar);
            Calendar result = calendarRepository.save(calendar);
            for (Organization o : calendar.getOrganizations()) {
                List<Calendar> cal;
                cal = o.getCalendars();
                cal.add(calendar);
                o.setCalendars(cal);
                organizationRepository.save(o);
            }
            publisher.publishEvent(new EventCreateEvent(result.getName()));
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

            Calendar toDelete = this.findById(id);
            List<Event> events = toDelete.getEvents();

            /**  for (Event e: events
             ) {

             eventService.delete(e);
             // eventEndpoint.deleteEvent(eventMapper.eventToEventDto(e));
             } **/

            List<Organization> olist = toDelete.getOrganizations();


            olist.forEach(it -> it.getCalendars().remove(toDelete));
            organizationRepository.saveAll(olist);

            toDelete.getOrganizations().removeAll(olist);

            if (this.findById(id).getEvents() != null) {

                for (Event e : toDelete.getEvents()) {

                    eventService.delete(e.getId());
                }

                List<Event> empty = new ArrayList<Event>();
                toDelete.setEvents(empty);

            }
            calendarRepository.delete(toDelete);

            publisher.publishEvent(new EventDeleteEvent(toDelete.getName()));
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Calendar update(Calendar calendar) {
        try {
            validator.validateUpdateCalendar(calendar);

            Calendar savedCalendar = calendarRepository.save(calendar);
            publisher.publishEvent(new EventCreateEvent(savedCalendar.getName()));
            return savedCalendar;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public Calendar updateOrganizationsWithList(Calendar calendar, List<Organization> organizations) {
        try {
            //remove calendar from all orgs it is associated, and save
            Optional<Calendar> currentCalendar = calendarRepository.findById(calendar.getId());
            if (currentCalendar.isPresent()) {
                List<Organization> savedOrgs = currentCalendar.get().getOrganizations();
                savedOrgs.forEach(org -> {
                    org.getCalendars().removeIf(cal -> cal.getId().equals(calendar.getId()));
                });
                organizationRepository.saveAll(savedOrgs);
            }
            //add calendar to all organisations, remove first to avoid double entries.
            organizations.forEach(org -> {
                org.getCalendars().removeIf(cal -> cal.getId().equals(calendar.getId()));
                org.getCalendars().add(calendar);
            });

            //save organizations to ensure data correctness. Save & return calendar.
            organizationRepository.saveAll(organizations);
            calendar.setOrganizations(new ArrayList<>(organizations));
            Calendar savedCalendar = calendarRepository.save(calendar);
            return savedCalendar;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }


    @Override
    public Calendar setCoverImage(Calendar calendar, byte[] imageBlob) {
        try {
            Byte[] byteArray = new Byte[imageBlob.length];
            for (int i = 0; i < imageBlob.length; i++) byteArray[i] = imageBlob[i];
            calendar.setCoverImage(byteArray);
            return calendarRepository.save(calendar);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
