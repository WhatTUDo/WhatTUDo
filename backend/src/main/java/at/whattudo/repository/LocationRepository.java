package at.whattudo.repository;

import at.whattudo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    /**
     * Find the Location in the db with the specified name.
     *
     * @param address address of the location.
     * @param zip     zip of the location
     * @return the location with the specified address and zip
     */
    Optional<Location> findByAddressAndZip(String address, String zip);

    /**
     * Find all locations in the db where the location-name contains a specific name/sequence of characters.
     *
     * @param name String to be tested against all location-names if these contain name.
     * @return list of matching location entries that contain name.
     */
    List<Location> findByNameContainingIgnoreCase(String name);

    /**
     * Find all locations in the db where the location-address contains a specific address/sequence of characters.
     *
     * @param address String to be tested against all location-addresses if these contain address.
     * @return list of matching location entries that contain address.
     */
    List<Location> findByAddressContainingIgnoreCase(String address);

}


