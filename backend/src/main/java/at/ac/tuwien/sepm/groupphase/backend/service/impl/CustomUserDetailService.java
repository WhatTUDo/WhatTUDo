package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.events.user.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LabelService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomUserDetailService implements UserService {
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final AttendanceService attendanceService;
    private final EventService eventService;
    private final LabelService labelService;
    private final LabelRepository labelRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override // TODO: Move encoding to mapper?
    public ApplicationUser saveNewUser(ApplicationUser user) throws ServiceException, ValidationException {
        try {
            validator.validateNewUser(user);
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            ApplicationUser savedUser = userRepository.save(user);
            publisher.publishEvent(new UserCreateEvent(savedUser.getUsername()));
            return savedUser;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            validator.validateUpdateUser(user);
            Optional<ApplicationUser> find = userRepository.findById(user.getId());
            if (find.isEmpty()) {
                throw new NotFoundException("User not found");
            }
            ApplicationUser old = find.get();
            if (user.getEmail() == null || user.getEmail().equals("")) {
                user.setEmail(old.getEmail());
            } else {
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    throw new ValidationException("A user already registered with this email!");
                }
            }

            if (user.getName() == null || user.getName().equals("")) {
                user.setName(old.getName());
            }
            user.setPassword(old.getPassword());
            ApplicationUser savedUser = userRepository.save(user);
            publisher.publishEvent(new UserUpdateEvent(savedUser.getUsername()));
            return savedUser;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationUser changeUserPassword(String username, String currentPassword, String newPassword) {
        try {
            validator.validateChangePassword(username, currentPassword, newPassword);
            Optional<ApplicationUser> foundUser = userRepository.findByName(username);
            if (foundUser.isEmpty()) {
                throw new NotFoundException("User does not exist");
            }
            if (!passwordEncoder.matches(currentPassword, foundUser.get().getPassword())) {
                throw new ValidationException("Wrong password!");
            }
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            foundUser.get().setPassword(encodedNewPassword);
            ApplicationUser savedUser = userRepository.save(foundUser.get());
            publisher.publishEvent(new UserPasswordChangeEvent(savedUser.getUsername()));
            return savedUser;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationUser updateRoleInOrga(ApplicationUser user, Organization organization, OrganizationRole organizationRole) {
        try {
            Organization dbOrga = organizationRepository.findById(organization.getId()).orElseThrow(() -> new IllegalArgumentException("Organization does not exist"));
            ApplicationUser dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User does not exist"));

            OrganizationMembership userMembership = new OrganizationMembership(dbOrga, dbUser, organizationRole);
            dbOrga.getMemberships().add(userMembership);
            dbUser.getMemberships().add(userMembership);

            organizationRepository.save(dbOrga);
            ApplicationUser savedUser = userRepository.save(dbUser);
            publisher.publishEvent(new UserRoleChangeEvent(savedUser.getUsername(), userMembership.getOrganization(), userMembership.getRole()));
            return savedUser;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApplicationUser removeFromOrga(ApplicationUser user, Organization organization) {
        try {
            Organization dbOrga = organizationRepository.findById(organization.getId()).orElseThrow(() -> new IllegalArgumentException("Organization does not exist"));
            ApplicationUser dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User does not exist"));

            dbOrga.getMemberships().removeIf(it -> it.getUser().getName().equals(dbUser.getName()));
            dbUser.getMemberships().removeIf(it -> it.getOrganization().getId().equals(dbOrga.getId()));

            ApplicationUser savedUser = userRepository.save(dbUser);
            organizationRepository.save(dbOrga);
            publisher.publishEvent(new UserRoleRemoveEvent(savedUser.getUsername(), dbOrga));
            return savedUser;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationUser findUserById(Integer id) throws NotFoundException, ServiceException {
        try {
            Optional<ApplicationUser> found = userRepository.findById(id);
            if(!found.isPresent()){
                throw new NotFoundException("Could not find any user with id"+id);
            }

            return found.get();
        }catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public ApplicationUser getUserByName(String name) throws ServiceException {
        try {
            Optional<ApplicationUser> found = userRepository.findByName(name);
            if (found.isEmpty()) {
                throw new NotFoundException("user not found");
            }
            return found.get();
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public List<Organization> getUserOrganizations(Integer userId) throws ServiceException {
        try {
            ApplicationUser applicationUser = userRepository.getOne(userId);
            Set<OrganizationMembership> organizationMemberships = applicationUser.getMemberships();
            List<Organization> organizations = new ArrayList<>();
            for (OrganizationMembership o : organizationMemberships) {
                organizations.add(o.getOrganization());
            }
            return organizations;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }

    }



    @Transactional
    @Override
    public Set<Event> getRecommendedEvents(Integer userId) {

        try {
            ArrayList<Integer> ids = new ArrayList<>();
            Set<Event> recommendedEvents = new HashSet<>();
            List<Event> events = attendanceService.getEventUserIsAttending(userId);
            events.addAll(attendanceService.getEventUserIsInterested(userId));
            Collection<Label> allLabels = labelService.getAll();
            int maxLabelId = 0;
            for (Label l : allLabels
            ) {
                maxLabelId = l.getId() > maxLabelId ? l.getId() : maxLabelId;
            }
            int[] labels = new int[maxLabelId + 1];
            events.forEach(event -> {
                List<Label> eventLabels = event.getLabels();
                eventLabels.forEach(label -> labels[label.getId()]++);
            });

            for (int i = 0; i < labels.length; i++) {
                int maxAt = 0;
                for (int j = 0; j < labels.length; j++) {
                    maxAt = labels[j] > labels[maxAt] ? j : maxAt;
                }

                if (labels[maxAt] != 0) {
                    List<Event> possibleEvents = labelService.findById(maxAt).getEvents();
                    Optional<Event> possibleEvent = possibleEvents.stream().filter(e -> e.getStartDateTime().isAfter(LocalDateTime.now()) && !attendanceService.getUsersByEvent(e).contains(userRepository.getOne(userId))).findAny();
                    if (possibleEvent.isPresent() && !ids.contains(possibleEvent.get().getId())) {
                        recommendedEvents.add(possibleEvent.get());
                        ids.add(possibleEvent.get().getId());
                    }
                    labels[maxAt] = 0;
                    if (recommendedEvents.size() >= 4) return recommendedEvents;
                }
            }
            for (int i = 0; i < 4 - recommendedEvents.size(); i++) {
                Optional<Event> randomEvent = eventService.findForDates(LocalDateTime.now(), LocalDateTime.now().plusDays(30)).stream().findAny();
                if (randomEvent.isPresent() && !ids.contains(randomEvent.get().getId())) {
                    recommendedEvents.add(randomEvent.get());
                } else {
                    return recommendedEvents;
                }
            }
            return recommendedEvents;
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
