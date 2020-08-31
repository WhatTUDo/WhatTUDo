package at.whattudo.service.impl;

import at.whattudo.entity.*;
import at.whattudo.events.location.LocationCreateEvent;
import at.whattudo.events.location.LocationDeleteEvent;
import at.whattudo.events.location.LocationUpdateEvent;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.EventRepository;
import at.whattudo.repository.LocationRepository;
import at.whattudo.service.LocationService;
import at.whattudo.util.ValidationException;
import at.whattudo.util.Validator;
import at.whattudo.entity.Event;
import at.whattudo.entity.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
            publisher.publishEvent(new LocationCreateEvent(location.getName()));
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
                publisher.publishEvent(new LocationUpdateEvent(location.getName()));
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
                publisher.publishEvent(new LocationDeleteEvent(toDelete.getName()));
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
