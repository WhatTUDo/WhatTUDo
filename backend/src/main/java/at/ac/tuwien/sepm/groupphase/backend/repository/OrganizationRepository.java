package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    /**
     * Find the organization in the db with the specified name.
     *
     * @param name name of the organization.
     * @return the organization with the specified name
     */
    Optional<Organization> findByName(String name);
}
