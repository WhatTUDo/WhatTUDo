package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.events.OrganisationCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.organisation.OrganisationEditEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganisationService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleOrganisationService implements OrganisationService {
    private final ApplicationEventPublisher publisher;
    private final OrganisationRepository organisationRepository;
    private final CalendarRepository calendarRepository;
    private final Validator validator;


    @Override
    public Collection<Organisation> getAll() {
        return organisationRepository.findAll();
    }

    @Override
    public Organisation findById(int id) {
        Optional<Organisation> found = organisationRepository.findById(id);
        if (found.isPresent()) {
            Organisation organisation = found.get();
            //TODO  publisher.publishEvent(new EventFindEvent(organisation.getName()));
            return organisation;
        } else {
            throw new NotFoundException("No organisation found with id " + id);
        }
    }

    @Override
    public Organisation create(Organisation organisation) {
        validator.validateNewOrganisation(organisation);
        try {
            Organisation saved = organisationRepository.save(organisation);
            publisher.publishEvent(new OrganisationCreateEvent(saved.getName()));
            return saved;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Organisation update(Organisation organisation) {

        log.info("Service update organisation {}", organisation);
        validator.validateUpdateOrganisation(organisation);

        try {
            Optional<Organisation> found = organisationRepository.findById(organisation.getId());
            if (found.isPresent()) {
                publisher.publishEvent(new OrganisationEditEvent(organisation.getName()));
                Organisation orgInDataBase = found.get();
                orgInDataBase.setName(organisation.getName());
                orgInDataBase.setCalendars(organisation.getCalendars());
                return organisationRepository.save(orgInDataBase);
            } else {
                throw new NotFoundException("No organisation found with id " + organisation.getId());
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Organisation addCalendars(Organisation organisation, Collection<Calendar> calendars) {
        log.info("Adding calendars {} to organisation {}", calendars, organisation);
        try {
            calendars.forEach(it -> it.getOrganisations().add(organisation));
            calendarRepository.saveAll(calendars);

            organisation.getCalendars().addAll(calendars);
            return organisationRepository.save(organisation);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Organisation removeCalendars(Organisation organisation, Collection<Calendar> calendars) {
        log.info("Removing calendars {} from organisation {}", calendars, organisation);
        try {
            calendars.forEach(it -> it.getOrganisations().remove(organisation));
            calendarRepository.saveAll(calendars);

            organisation.getCalendars().removeAll(calendars);
            return organisationRepository.save(organisation);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
