package at.ac.tuwien.sepm.groupphase.backend.servicetests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.util.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class LocationServiceTest {

    @Autowired
    LocationService service;
    @Autowired
    LocationRepository repository;

    @Test
    public void save_shouldReturn_sameLocation() {

        Location locationEntity = new Location("Fachschaft Informatik", "Funkgasse 17", "1180", 45, 80);
        Location gottenLocation = service.save(locationEntity);

        assertEquals(locationEntity.getName(), gottenLocation.getName());
        assertEquals(locationEntity.getAddress(), gottenLocation.getAddress());
        assertEquals(locationEntity.getZip(), gottenLocation.getZip());
        assertEquals(locationEntity.getLatitude(), gottenLocation.getLatitude());
        assertEquals(locationEntity.getLongitude(), gottenLocation.getLongitude());
    }


    @Test
    public void save_thenRead_shouldReturn_sameLocation() {
        Location locationEntity = new Location("Fachschaft Wirtschaftsinformatik", "Funkgasse 17/2", "1180", 45.2, 80.2);
        Location returnedLocation = service.save(locationEntity);
        Location gottenLocation = service.findById(returnedLocation.getId());

        assertEquals(returnedLocation.getId(), gottenLocation.getId());
        assertEquals(returnedLocation.getName(), gottenLocation.getName());
        assertEquals(returnedLocation.getAddress(), gottenLocation.getAddress());
        assertEquals(returnedLocation.getZip(), gottenLocation.getZip());
        assertEquals(returnedLocation.getLongitude(), gottenLocation.getLongitude());
        assertEquals(returnedLocation.getLatitude(), gottenLocation.getLatitude());

    }



    @Test
    public void save_withoutCorrectParam_shouldReturn_ValidationException() {

        Location location1 = new Location("","","1180",12,15);
        Location location2 = new Location("Bäckerei Frühspaß","Brotbackstraße 1","",15,8);
        Location location3 = new Location("public Bathroom","open field street 14","O14528-7",-225,800);

        assertThrows(ValidationException.class, () -> service.save(location1));
        assertThrows(ValidationException.class, () -> service.save(location2));
        assertThrows(ValidationException.class, () -> service.save(location3));
        assertThrows(ValidationException.class, () -> service.save(null));
    }

    @Test
    public void delete_nonSavedEvent_IdNotGenerated_throwsValidationException() {
        assertThrows(ValidationException.class, () -> service.delete(0));
    }

    @Transactional
    @Test
    public void delete_savedEvent_findBYIdReturnsNotFound() {

        Location locationEntity = new Location("Mensa", "Glashaus 1", "8080", 13.5, -120.8);
        Location location = service.save(locationEntity);
        service.delete(location.getId());
        assertThrows(NotFoundException.class, () -> service.findById(location.getId()));
    }


    @Test
    public void getAll_returnsAll() {
        service.save(new Location("Audimax", "2. Aufgang dann rechts", "1180", 13.53, 1.8));
        service.save(new Location("Audmaximus","1.Aufgang dann links","1180",-180,180));
        assert(service.getAll().size() == 2);

    }

    @Test
    public void save_thenUpdated_thenRead_shouldReturn_updatedLocation() {
        Location location = repository.save(new Location("Alteshaus", "Haustraße 25/7/5", "1010", 1, 2));
        Location updatedLocation = new Location("Neueshaus", "Haustraße 25/7/5", "1010", 1, 2);
        updatedLocation.setId(location.getId());
        Location returnedLocation = service.update(updatedLocation);

        assertEquals(updatedLocation.getName(),returnedLocation.getName());
        assertEquals(returnedLocation.getId(),location.getId());


    }


}
