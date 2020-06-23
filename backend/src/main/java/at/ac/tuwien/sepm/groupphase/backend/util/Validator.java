package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
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
        if ((event.getEndDateTime().getYear() < 2020)) {
            result += "End Date is not valid. Year is before this calendar even existed. \n";
        }


        if (!(result.equals("Following problems with this input: \n"))) {

            throw new ValidationException(result);
        }
    }

    public void validateUpdateOrganization(Organization organization) {
        if (organization.getName().isBlank()) throw new ValidationException("Organization name must not be empty.");
    }

    public void validateMultipleEventsQuery(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new ValidationException("End Date must be after Start Date");

    }

    public void validateNewOrganization(Organization organization) {

        String result = "Following problems with this input: \n";

        if (organization.getName().isBlank()) {
            result += "Organisation name must not be empty. \n";
            //throw new ValidationException("Organization name must not be empty.");
        }

        if (organization.getId() != null && organizationRepository.findById(organization.getId()).isPresent()) {
            result += "An Organization with this ID already exists. \n";
            //throw new ValidationException("An Organisation with this ID already exists");
        }
        if (organizationRepository.findByName(organization.getName()).isPresent()) {

            result += "An Organization with this Name already exists. \n";
            //throw new ValidationException("An Organisation with this Name already exists");
        }

        if (!(result.equals("Following problems with this input: \n"))) {
            throw new ValidationException(result);
        }
    }

    public void validateChangePassword(String username, String currentPassword, String newPassword) {
        List<Exception> exceptions = new ArrayList<>();
        if (username == null) {
            exceptions.add(new ValidationException("Username cannot be null or empty!"));
        }
        if (currentPassword == null) {
            exceptions.add(new ValidationException("Current password cannot be null"));
        }// TODO: Add password validation
        if (newPassword == null) {
            exceptions.add(new ValidationException("Current password cannot be null"));
        } // TODO: Add password validation

        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }


    }

    public void validateUpdateUser(ApplicationUser user) {
        List<Exception> exceptions = new ArrayList<>();
        if (user == null) {
            exceptions.add(new ValidationException("User cannot be null"));
        }
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            if (!emailIsValid(user.getEmail())) {
                exceptions.add(new ValidationException("Email is not in a valid format!"));
            }
        }
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
        } else {
            if (!emailIsValid(user.getEmail())) {
                exceptions.add(new ValidationException("Email is not in a valid format!"));
            }
        }

        if (exceptions.size() > 0) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }
    }


    public void validateUpdateCalendar(Calendar calendar) {
        List<Exception> exceptions = new ArrayList<>();
        if (calendar == null) {
            exceptions.add(new ValidationException("Calendar cannot be null"));
        }
        if (calendar.getName().isBlank()) {
            exceptions.add(new ValidationException("Name must not be empty"));
        }
        if (calendar.getOrganizations().isEmpty()) {
            exceptions.add(new ValidationException("Calendar must belong to an organization"));
        }
        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }
    }

    public void validateNewCalendar(Calendar calendar) {
        List<Exception> exceptions = new ArrayList<>();
        if (calendar == null) {
            exceptions.add(new ValidationException("Calendar cannot be null"));
        }
        if (calendar.getName().equals("")) {
            exceptions.add(new ValidationException("Name must not be empty"));
        }
        if (calendar.getOrganizations().isEmpty()) {
            exceptions.add(new ValidationException("Calendar must belong to an organization"));
        }
        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }
    }

    public void validateUpdateEvent(Event event) {
        List<Exception> exceptions = new ArrayList<>();
        if (event.getName().isBlank()) {
            exceptions.add(new ValidationException("Name must not be empty"));
        }
        /**  please don't do this! you should be able to update a currently running event. (this is probably the most likely time for update)
         if ((event.getStartDateTime().isBefore(LocalDateTime.now()))) {
         exceptions.add(new ValidationException("Start date must not be in the past"));
         }
         **/
//        NOTE: this.is not working. caused me a 422 though start was before end - probably only checks date not time
        if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
            exceptions.add(new ValidationException("Start date must be before end date"));
        }

        if (event.getStartDateTime().getYear() < 2020) {
            exceptions.add(new ValidationException("Start Date cannot be before the year 2020!"));
        }


        if (!exceptions.isEmpty()) {
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

    public void validateLocation(Location location) {
        List<Exception> exceptions = new ArrayList<>();
        if (location==null) {
            throw new ValidationException("Location must not be null");
        }
        if (location.getName().isBlank()) {
            exceptions.add(new ValidationException("Name must not be empty"));
        }
        if (location.getAddress().isBlank()) {
            exceptions.add(new ValidationException("Address must not be empty"));
        }
        if (location.getZip().isBlank()) {
            exceptions.add(new ValidationException("ZIP-Code must not be empty"));
        }
        if (location.getLatitude()>180 || location.getLatitude()<-180) {
            exceptions.add(new ValidationException("Invalid Latitude"));
        }
        if (location.getLongitude() > 180 || location.getLongitude() < -180) {
            exceptions.add(new ValidationException("Invalid Longitude"));
        }

        if (!exceptions.isEmpty()) {
            String summary = createExceptionSummaryString(exceptions);
            throw new ValidationException(summary);
        }
    }

    public void validateSearchTerm(String searchTerm) {
        if (searchTerm.equals(null) || searchTerm == "") {
            throw new ValidationException("Search String cannot be null or Empty!");
        }
    }
}
