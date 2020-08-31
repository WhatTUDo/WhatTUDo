package at.whattudo.repository;

import at.whattudo.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class OldUserRepository {

    private final ApplicationUser user;
    private final ApplicationUser admin;

    @Autowired
    public OldUserRepository(PasswordEncoder passwordEncoder) {
        user = new ApplicationUser("user", "user@email.com", passwordEncoder.encode("password"));
        admin = new ApplicationUser("admin", "admin@email.com", passwordEncoder.encode("password"));
    }

}
