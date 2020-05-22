package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
}
