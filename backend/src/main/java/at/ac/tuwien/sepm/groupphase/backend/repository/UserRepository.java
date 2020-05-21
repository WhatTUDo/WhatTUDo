package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    List<ApplicationUser> findByName(String name);

    List<ApplicationUser> findAll();

    List<ApplicationUser> findByEmail(String email);

}
