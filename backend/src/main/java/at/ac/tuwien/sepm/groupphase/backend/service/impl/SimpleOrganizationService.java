package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.events.organization.OrganizationCalendarAddEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.organization.OrganizationCalendarRemoveEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.organization.OrganizationCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.organization.OrganizationEditEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrganizationService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleOrganizationService implements OrganizationService {
    private final ApplicationEventPublisher publisher;
    private final OrganizationRepository organizationRepository;
    private final CalendarRepository calendarRepository;
    private final Validator validator;


    @Override
    public Collection<Organization> getAll() {
        try {
            return organizationRepository.findAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public Organization findById(int id) {
        try {
            Optional<Organization> found = organizationRepository.findById(id);
            if (found.isPresent()) {
                return found.get();
            } else {
                throw new NotFoundException("No organization found with id " + id);
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Organization create(Organization organization) {
        validator.validateNewOrganization(organization);
        try {
            Organization saved = organizationRepository.save(organization);
            publisher.publishEvent(new OrganizationCreateEvent(saved.getName()));
            return saved;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Organization update(Organization organization) {
        validator.validateUpdateOrganization(organization);
        try {
            Optional<Organization> found = organizationRepository.findById(organization.getId());
            if (found.isPresent()) {
                Organization orgInDataBase = found.get();
                orgInDataBase.setName(organization.getName());
                orgInDataBase.setCalendars(organization.getCalendars());
                Organization savedOrga = organizationRepository.save(orgInDataBase);
                publisher.publishEvent(new OrganizationEditEvent(savedOrga.getName()));
                return savedOrga;
            } else {
                throw new NotFoundException("No organization found with id " + organization.getId());
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

    @Override
    public Integer delete(Integer organisationID) throws ServiceException, NotFoundException {
        try {
            Organization organizationToDelete = this.findById(organisationID);
            List<Calendar> calendarsToRemove = organizationToDelete.getCalendars();
            this.removeCalendars(organizationToDelete, calendarsToRemove);
            organizationRepository.delete(organizationToDelete);
            return organisationID;
        }
        catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }
        catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Organization addCalendars(Organization organization, Collection<Calendar> calendars) {
        try {
            calendars.forEach(it -> it.getOrganizations().add(organization));
            calendarRepository.saveAll(calendars);

            organization.getCalendars().addAll(calendars);
            Organization savedOrga = organizationRepository.save(organization);
            publisher.publishEvent(new OrganizationCalendarAddEvent(savedOrga.getName(), calendars.stream().map(Calendar::getName).collect(Collectors.toList())));
            return savedOrga;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Organization> findByName(String name) {
        try {
            Optional<Organization> found = organizationRepository.findByName(name);
            List<Organization> organizations = new ArrayList<>();
            found.ifPresent(organizations::add);
            return organizations;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public Organization removeCalendars(Organization organization, Collection<Calendar> calendars) {
        try {
            List<Integer> calendarIds = calendars.stream().map(BaseEntity::getId).collect(Collectors.toList());
            calendarRepository.findAllById(calendarIds).forEach(it -> {
                it.getOrganizations().remove(organization);
                calendarRepository.save(it);
            });

            Organization dbOrga = organizationRepository.findById(organization.getId()).get();
            dbOrga.getCalendars().removeIf(it -> calendarIds.contains(it.getId()));
            Organization savedOrga = organizationRepository.save(dbOrga);
            publisher.publishEvent(new OrganizationCalendarRemoveEvent(savedOrga.getName(), calendars.stream().map(Calendar::getName).collect(Collectors.toList())));
            return savedOrga;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void test() {
    }

    @Transactional
    @Override
    public List<ApplicationUser> getMembers(Integer id) throws ServiceException{
      try {  Organization organization = organizationRepository.getOne(id);
        Set<OrganizationMembership> organizationMembershipSet =organization.getMemberships();
        List<ApplicationUser> members = new ArrayList<>();

        for (OrganizationMembership o: organizationMembershipSet){
            members.add(o.getUser());
        }
        return members;
      } catch (PersistenceException | IllegalArgumentException e){
          throw new ServiceException(e.getMessage());
      }
    }
}
