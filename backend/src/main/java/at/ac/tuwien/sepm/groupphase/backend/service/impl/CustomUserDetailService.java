package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.events.user.UserCreateEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.user.UserPasswordChangeEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.user.UserUpdateEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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

import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomUserDetailService implements UserService {
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
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
                if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
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
}
