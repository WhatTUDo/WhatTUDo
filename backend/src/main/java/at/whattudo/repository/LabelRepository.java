package at.whattudo.repository;

import at.whattudo.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {


    /**
     * Find the label in the db with the specified name.
     *
     * @param name name of the label.
     * @return the label with the specified name
     */
    Optional<Label> findByName(String name);

}
