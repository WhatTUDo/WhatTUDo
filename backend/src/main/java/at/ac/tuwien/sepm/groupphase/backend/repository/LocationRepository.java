package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    /**
     * Find the Location in the db with the specified name.
     *
     * @param address address of the location.
     * @param zip zip of the location
     * @return the location with the specified address and zip
     */
    Optional<Location> findByAddressAndZip(String address, String zip);



}


