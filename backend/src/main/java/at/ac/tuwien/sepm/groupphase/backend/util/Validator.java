package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    public void validateNewEvent(Event event) {
        if (event == null) {
            throw new ValidationException("Event object must not be null!");
        }
        if (event.getName().equals("")) throw new ValidationException("Event name must not be empty.");
        if (event.getEndDateTime().compareTo(event.getStartDateTime()) < 0)
            throw new ValidationException("end-date must be after start-date");
    }

    public void validateUpdateOrganisation(Organisation organisation) {
        if (organisation.getName().equals("")) throw new ValidationException("Event name must not be empty.");
    }


}
