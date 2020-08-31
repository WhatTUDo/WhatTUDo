package at.whattudo.repository;

import at.whattudo.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * Find all organizations in the db where the organization-name contains a specific name/sequence of characters.
     *
     * @param name String to be tested against all organization-names if these contain name.
     * @return list of matching organization entries that contain name.
     */
    List<Organization> findByNameContainingIgnoreCase(String name);
}
