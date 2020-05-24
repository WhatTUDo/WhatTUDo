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

        String result = "Following problems with this input: \n";

        if (event == null) {

          throw new ValidationException("Event object must not be null!");
        }

        if (event.getName().isBlank()) {
            result += "Event name must not be empty.\n";
            //throw new ValidationException("Event name must not be empty.");
            }
        if (!(event.getStartDateTime().isBefore(event.getEndDateTime()))) {
            result += "End Date must be after Start Date. \n";
           // throw new ValidationException("End Date must be after Start Date");
        }
        if((event.getEndDateTime().getYear() < 2020)){
            result += "End Date is not valid. Year is before this calendar even existed. \n";
        }


        if (!(result.equals("Following problems with this input: \n"))){

            throw new ValidationException(result);
        }
    }

    public void validateUpdateOrganisation(Organisation organisation) {
        if (organisation.getName().isBlank()) throw new ValidationException("Organisation name must not be empty.");
    }

    public void validateMultipleEventsQuery(String name, LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new ValidationException("End Date must be after Start Date");

    }


    public void validateNewOrganisation(Organisation organisation) {

        String result = "Following problems with this input: \n";

        if (organisation.getName().isBlank()) {
            result += "Organisation name must not be empty. \n";
            //throw new ValidationException("Organisation name must not be empty.");
        }
        if (organisationRepository.findById(organisation.getId()).isPresent()) {
            result += "An Organisation with this ID already exists. \n";
            //throw new ValidationException("An Organisation with this ID already exists");
        }
        if (organisationRepository.findByName(organisation.getName()).isPresent()) {

            result += "An Organisation with this Name already exists. \n";
            //throw new ValidationException("An Organisation with this Name already exists");
        }

        if (!(result.equals("Following problems with this input: \n"))){

            throw new ValidationException(result);
        }
    }
}
