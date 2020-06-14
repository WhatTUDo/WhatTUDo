package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleLabelService implements LabelService {
    private final ApplicationEventPublisher publisher;
    private final LabelRepository labelRepository;
    private final EventRepository eventRepository;
    private final Validator validator;

    //FIXME: catch persistance throw service
    @Override
    public Collection<Label> getAll() {
        return labelRepository.findAll();
    }

    //FIXME: catch persistance throw service
    @Override
    public Label findById(int id) {
        Optional<Label> found = labelRepository.findById(id);
        if (found.isPresent()) {
            Label label = found.get();
            //TODO  publisher.publishEvent(new EventFindEvent(organization.getName()));
            return label;
        } else {
            throw new NotFoundException("No label found with id " + id);
        }
    }


    @Transactional
    @Override
    public void delete(Integer id) {
        try {
            if (id != null) {
                this.findById(id);
            } else {
                throw new ValidationException("Id is not defined");
            }
            //   publisher.publishLabel(new EventDeleteLabel(this.findById(id).getName())); sth like this?

            Label toDelete = this.findById(id);
            List<Event> elist = toDelete.getEvents();

            try {
                elist.forEach(it -> it.getLabels().remove(toDelete));
                eventRepository.saveAll(elist);

                toDelete.getEvents().removeAll(elist);


                List<Event> empty = new ArrayList<Event>();
                toDelete.setEvents(empty);


                labelRepository.delete(toDelete);

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
    public Label save(Label label) {
        try {
            Label result = labelRepository.save(label);


            //   publisher.publishEvent(new EventCreateLabel(label.getName())); ???
            return result;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Label update(Label label) {
        try {

            Label toUpdate = this.findById(label.getId());

            if (!(label.getName().isBlank()) || !(label.getName().equals((this.findById(label.getId())).getName()))) {
                toUpdate.setName(label.getName());
            }

            toUpdate.setEvents(this.findById(label.getId()).getEvents());


            //    publisher.publishEvent(new EventUpdateLabel(label.getName()));
            return labelRepository.save(toUpdate);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Label> findByEventId(int id) {

        List<Label> result = new ArrayList<Label>();

        Optional<Event> found = eventRepository.findById(id);

        if (found.isPresent()) {
            Event finditslabels = found.get();


            labelRepository.findAll().forEach(it -> {
                if ((it.getEvents().contains(finditslabels)))
                    result.add(it);
            });
        } else {
            throw new NotFoundException();
        }

        return result;

    }

}
