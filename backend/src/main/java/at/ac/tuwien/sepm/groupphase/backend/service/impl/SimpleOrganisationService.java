package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventDeleteEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.organisation.OrganisationEditEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleOrganisationService implements OrganisationService {
    private final ApplicationEventPublisher publisher;
    private final OrganisationRepository organisationRepository;
    private final Validator validator;


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
}
