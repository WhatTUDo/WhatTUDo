package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.LocationEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LabelDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LabelMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Label;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LabelRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@DirtiesContext
public class LocationEndpointTest {

    @Autowired
    LocationEndpoint endpoint;

    @Autowired
    UserRepository userRepository;


    @Autowired
    LocationMapper mapper;


    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void getAllLocations_shouldReturn_All() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto1 = new LocationDto(1, "Abermals ne location", "Bahnstraße 2", "1100", 1, 1, eventIds);
        LocationDto locationDto2 = new LocationDto(2, "die sollte nicht gefunden werden", "Blubplatz 15", "1100", 1, 1, eventIds);
        endpoint.create(locationDto1);
        endpoint.create(locationDto2);
        List<LocationDto> foundLocations = endpoint.getAllLocations();
        assert (foundLocations.size() >= 2);
    }

    @WithMockUser(username = "User 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_shouldReturn_sameLocation() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto = new LocationDto(1, "Test Name", "Test address", "1100", 1, 1, eventIds);
        LocationDto returnedLocation = endpoint.create(locationDto);

        assertEquals(locationDto.getName(), returnedLocation.getName());
        assertEquals(locationDto.getAddress(), returnedLocation.getAddress());
        assertEquals(locationDto.getZip(), returnedLocation.getZip());
        assertEquals(locationDto.getLatitude(), returnedLocation.getLatitude());
        assertEquals(locationDto.getLongitude(), returnedLocation.getLongitude());
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_thenRead_shouldReturn_sameLocation() {

        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto = new LocationDto(1, "Test Name", "Test address", "1100", 1, 1, eventIds);
        LocationDto returnedLocation = endpoint.create(locationDto);
        LocationDto gottenLocation = endpoint.getById(returnedLocation.getId());

        assertEquals(gottenLocation.getName(), returnedLocation.getName());
        assertEquals(gottenLocation.getAddress(), returnedLocation.getAddress());
        assertEquals(gottenLocation.getZip(), returnedLocation.getZip());
        assertEquals(gottenLocation.getLatitude(), returnedLocation.getLatitude());
        assertEquals(gottenLocation.getLongitude(), returnedLocation.getLongitude());

    }


    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void save_withoutCorrectParam_shouldReturn_ResponseStatusException() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto1 = new LocationDto(null, "", "Test address", "1100", 1, 1, eventIds);
        LocationDto locationDto2 = new LocationDto(null, "Test Name", "Test address", "1100", 1, 200, eventIds);

        assertThrows(ResponseStatusException.class, () -> endpoint.create(locationDto1));
        assertThrows(ResponseStatusException.class, () -> endpoint.create(locationDto2));
    }

    @Transactional
    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void delete_savedEvent_findByIdReturnsResponseException() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto = new LocationDto(1, "Test Name", "Test address", "1100", 1, 1, eventIds);
        LocationDto returnedLocation = endpoint.create(locationDto);
        endpoint.delete(returnedLocation.getId());
        assertThrows(ResponseStatusException.class, () -> endpoint.getById(returnedLocation.getId()));

    }


    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void updateEntityValues_shouldReturn_correctChanges() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto = new LocationDto(1, "Test Name", "Test address", "1100", 1, 1, eventIds);
        LocationDto returnedLocation = endpoint.create(locationDto);

        assertEquals(locationDto.getName(), returnedLocation.getName());
        assertEquals(locationDto.getAddress(), returnedLocation.getAddress());
        assertEquals(locationDto.getZip(), returnedLocation.getZip());
        assertEquals(locationDto.getLatitude(), returnedLocation.getLatitude());
        assertEquals(locationDto.getLongitude(), returnedLocation.getLongitude());

        LocationDto locationDtoChanges = new LocationDto(returnedLocation.getId(), "Test Name 2", "Test address 2", "1102", 2, 2, eventIds);
        LocationDto finalLocation = endpoint.update(locationDtoChanges);

        assertEquals(finalLocation.getName(), locationDtoChanges.getName());
        assertEquals(finalLocation.getAddress(), locationDtoChanges.getAddress());
        assertEquals(finalLocation.getZip(), locationDtoChanges.getZip());
        assertEquals(finalLocation.getLatitude(), locationDtoChanges.getLatitude());
        assertEquals(finalLocation.getLongitude(), locationDtoChanges.getLongitude());

    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void findByName_shouldReturn_correctLocation() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto1 = new LocationDto(1, "Test Name", "Test address", "1100", 1, 1, eventIds);
        LocationDto locationDto2 = new LocationDto(2, "Nicht findbar", "Test address", "1100", 1, 1, eventIds);
        LocationDto returnedLocation1 = endpoint.create(locationDto1);
        LocationDto returnedLocation2 = endpoint.create(locationDto2);

        List<LocationDto> foundLocation = endpoint.searchLocationNames("Test N");
        assert (foundLocation.size() > 0);
        assertEquals(locationDto1.getName(), foundLocation.get(0).getName());

        List<LocationDto> nonFound = endpoint.searchLocationNames("Brotbackstube");
        assert (nonFound.size() == 0);
    }

    @WithMockUser(username = "Person 1", authorities = {"MOD_1", "MEMBER_1"})
    @Test
    public void findByAddress_shouldReturn_correctLocation() {
        List<Integer> eventIds = Collections.emptyList();
        LocationDto locationDto1 = new LocationDto(1, "Alteshaus", "Haustraße 25/7/5", "1100", 1, 1, eventIds);
        LocationDto locationDto2 = new LocationDto(2, "Nicht findbares Haus", "keine Adresse", "1100", 1, 1, eventIds);
        LocationDto returnedLocation1 = endpoint.create(locationDto1);
        LocationDto returnedLocation2 = endpoint.create(locationDto2);

        List<LocationDto> foundLocation = endpoint.searchLocationAddress("Haustraße");
        assert (foundLocation.size() > 0);
        assertEquals(locationDto1.getName(), foundLocation.get(0).getName());

        List<LocationDto> nonFound = endpoint.searchLocationAddress("Brotbackstube");
        assert (nonFound.size() == 0);
    }


}

