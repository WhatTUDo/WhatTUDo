package at.whattudo.repository;

import at.whattudo.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    /**
     * Find all users in the db
     *
     * @return list of all user entries in the db.
     */
    List<ApplicationUser> findAll();

    /**
     * Find a user in the db by name.
     *
     * @param name name of the user entry.
     * @return the user entry.
     */
    Optional<ApplicationUser> findByName(String name);

    /**
     * Find a user in the db by email.
     *
     * @param email email of the user entry.
     * @return the user entry.
     */
    Optional<ApplicationUser> findByEmail(String email);

    /**
     * Find a user in the db by id.
     *
     * @param id id of the user entry.
     * @return the user entry.
     */
    Optional<ApplicationUser> findById(Integer id);

}
