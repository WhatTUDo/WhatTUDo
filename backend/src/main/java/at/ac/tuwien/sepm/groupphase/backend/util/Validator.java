package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public void validateUpdateUser(ApplicationUser user){
        List<Exception> exceptions = new ArrayList<>();
        if(user == null){ exceptions.add(new ValidationException("User cannot be null")); }
        if(user.getEmail() != null && !user.getEmail().equals("")){
            if (!emailIsValid(user.getEmail())) {
                exceptions.add(new ValidationException("Email is not in a valid format!"));
            }
        }
        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }

    }
    public void validateChangePassword(String email, String currentPassword, String newPassword ){
        List<Exception> exceptions = new ArrayList<>();
        if(email == null){
            exceptions.add(new ValidationException("Email cannot be null or empty!"));
        }else{
            if(!emailIsValid(email)){
                exceptions.add(new ValidationException("Email is not in a valid format!"));
            }
        }
        if(currentPassword == null){
            exceptions.add(new ValidationException("Current password cannot be null"));
        }// FIXME: Add password validation
        if(newPassword == null){
            exceptions.add(new ValidationException("Current password cannot be null"));
        } // FIXME: Add password validation

        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }


}
    public void validateNewUser(ApplicationUser user) {
        List<Exception> exceptions = new ArrayList<>();
        if (user == null) exceptions.add(new ValidationException("User cannot be null!"));
        if (user.getName().isBlank()) exceptions.add(new ValidationException("Username cannot be null or empty!"));
        if (user.getPassword().isBlank()) exceptions.add(new ValidationException("Password cannot be null or empty!"));
        if (user.getEmail().isBlank()) {
            exceptions.add(new ValidationException("Email cannot be null or empty!"));
        }
        else {
            if (!emailIsValid(user.getEmail())) {
                exceptions.add(new ValidationException("Email is not in a valid format!"));
            }
        }

        if (exceptions.size() > 0) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }
    }

    private String createExceptionSummaryString(List<Exception> exceptions) {
        String summaryString = "";
        for (Exception exception : exceptions) {
            summaryString += exception.getLocalizedMessage() + "\n ";
        }

        return summaryString;
    }

    private boolean emailIsValid(String emailString) {
        String mailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(mailRegex);
        Matcher matcher = pattern.matcher(emailString);
        return matcher.matches();
    }
}
