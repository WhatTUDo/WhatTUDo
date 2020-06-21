package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventUpdateEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotAllowedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AttendanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleLocationService implements LocationService {
    private final ApplicationEventPublisher publisher;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final Validator validator;

    @Override
    public Collection<Location> getAll() throws ServiceException {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(int id) {
        Optional<Location> found = locationRepository.findById(id);
        if (found.isPresent()) {
            return found.get();
        } else {
            throw new NotFoundException("No location found with id " + id);
        }
    }

    @Override
    public Location save(Location location) throws ServiceException, ValidationException {
        try {
            validator.validateLocation(location);
            Optional<Location> found = locationRepository.findByAddressAndZip(location.getAddress(), location.getZip());
            return found.orElseGet(() -> locationRepository.save(location));
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Location update(Location location) {
        try {
            Optional<Location> found = locationRepository.findById(location.getId());
            if (found.isPresent()) {
                validator.validateLocation(location);
                //TODO: publisher.publishEvent(new EventUpdateEvent(savedEvent.getName()));
                return locationRepository.save(location);
            } else {
                throw new NotFoundException("No location found with id " + location.getId());
            }
        } catch (IllegalArgumentException | InvalidDataAccessApiUsageException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            if (id == null || id <= 0) throw new ValidationException("Id is not defined");
            Location toDelete = this.findById(id);
            List<Event> elist = toDelete.getEvents();

            try {
                elist.forEach(it -> it.setLabels(null));
                eventRepository.saveAll(elist);
                toDelete.getEvents().removeAll(elist);
                List<Event> empty = new ArrayList<>();
                toDelete.setEvents(empty);
                locationRepository.delete(toDelete);
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
    public List<Location> searchForName(String name) throws ServiceException {
        try {
            return locationRepository.findByNameContainingIgnoreCase(name);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Location> searchForAddress(String address) throws ServiceException {
        try {
            return locationRepository.findByAddressContainingIgnoreCase(address);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


}
