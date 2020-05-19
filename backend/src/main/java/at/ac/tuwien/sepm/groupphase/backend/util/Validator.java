package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Validator {
    private final OrganizationRepository organizationRepository;
    public void validateNewEvent(Event event) {
        if (event == null) {
            throw new ValidationException("Event object must not be null!");
        }
        if (event.getName().isBlank()) throw new ValidationException("Event name must not be empty.");
        if (event.getEndDateTime().compareTo(event.getStartDateTime()) < 0)
            throw new ValidationException("End Date must be after Start Date");
    }

    public void validateUpdateOrganization(Organization organization) {
        if (organization.getName().isBlank()) throw new ValidationException("Organization name must not be empty.");
    }

    public void validateMultipleEventsQuery(String name, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new ValidationException("End Date must be after Start Date");

    }


    public void validateNewOrganization(Organization organization) {
        if (organization.getName().isBlank()) throw new ValidationException("Organization name must not be empty.");
        if (organizationRepository.findById(organization.getId()).isPresent()) throw new ValidationException("An Organization with this ID already exists");
        if (organizationRepository.findByName(organization.getName()).isPresent()) throw new ValidationException("An Organization with this Name already exists");
    }
}
