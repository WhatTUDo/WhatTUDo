package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Validator {
    private final OrganisationRepository organisationRepository;
    public void validateNewEvent(Event event) {
        if (event == null) {
            throw new ValidationException("Event object must not be null!");
        }
        if (event.getName().isBlank()) throw new ValidationException("Event name must not be empty.");
        if (event.getEndDateTime().compareTo(event.getStartDateTime()) < 0)
            throw new ValidationException("End Date must be after Start Date");
    }

    public void validateUpdateOrganisation(Organisation organisation) {
        if (organisation.getName().isBlank()) throw new ValidationException("Organisation name must not be empty.");
    }

    public void validateMultipleEventsQuery(String name, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new ValidationException("End Date must be after Start Date");

    }


    public void validateNewOrganisation(Organisation organisation) {
        if (organisation.getName().isBlank()) throw new ValidationException("Organisation name must not be empty.");
        if (organisationRepository.findById(organisation.getId()).isPresent()) throw new ValidationException("An Organisation with this ID already exists");
        if (organisationRepository.findByName(organisation.getName()).isPresent()) throw new ValidationException("An Organisation with this Name already exists");
    }
}
