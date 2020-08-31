package at.whattudo.unittests;

import at.whattudo.entity.Location;
import at.whattudo.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext
public class LocationRepositoryTest {
    @Autowired
    LocationRepository locationRepository;


    @Test
    public void repoBasics() {

        assertThrows(InvalidDataAccessApiUsageException.class, () -> locationRepository.save(null));
        assertThrows(NullPointerException.class, () -> locationRepository.save(new Location(null, null, null, 0, 0)));
        locationRepository.save(new Location("Abstellraum", "Favoritenstraße 9", "1220", 120.05, 25.01));
        locationRepository.save(new Location("Zweiraumwohnung", "Karlsplatz 14/2", "1130", 2, 180));
    }


    @Test
    public void locationBasics() {

        Location location = locationRepository.save(new Location("Partykeller", "Buschmannstraße 12", "2230", 11.0575, 37.8852));

        assertEquals("Partykeller", location.getName());
        assertEquals("Buschmannstraße 12", location.getAddress());
        assertEquals("2230", location.getZip());
        assertEquals(11.0575, location.getLatitude());
        assertEquals(37.8852, location.getLongitude());
        locationRepository.delete(location);
        assertEquals(Optional.empty(), locationRepository.findById(location.getId()));
    }


}
