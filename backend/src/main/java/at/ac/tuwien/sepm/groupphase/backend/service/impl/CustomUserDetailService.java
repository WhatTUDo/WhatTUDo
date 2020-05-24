package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Optional;
import java.lang.invoke.MethodHandles;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomUserDetailService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public ApplicationUser saveNewUser(ApplicationUser user) throws ServiceException, ValidationException {
        try {
            validator.validateNewUser(user);
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return this.userRepository.save(user);
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
            if(find.isEmpty()){
                throw new NotFoundException("User not found");
           }
            ApplicationUser old = find.get();
            if (user.getEmail() == null || user.getEmail().equals("")) {
                user.setEmail(old.getEmail());
            }else{
                if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
                    throw new ValidationException("A user already registered with this email!");
                }
            }

            if (user.getName() == null || user.getName().equals("")){
              user.setName(old.getName());
            }
            user.setPassword(old.getPassword());
                return userRepository.save(user);
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationUser changeUserPassword(String email, String currentPassword, String newPassword) {
        try {
            validator.validateChangePassword(email, currentPassword, newPassword);
            Optional<ApplicationUser> foundUser = userRepository.findByEmail(email);
            if (foundUser.isEmpty()) {
                throw new NotFoundException("User does not exist");
            }
            if (!passwordEncoder.matches(currentPassword, foundUser.get().getPassword())) {
                throw new ValidationException("Wrong password!");
            }
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            foundUser.get().setPassword(encodedNewPassword);
            return userRepository.save(foundUser.get());
        } catch (PersistenceException | IllegalArgumentException e) {
            throw new ServiceException(e.getMessage());
        }

    }


}
