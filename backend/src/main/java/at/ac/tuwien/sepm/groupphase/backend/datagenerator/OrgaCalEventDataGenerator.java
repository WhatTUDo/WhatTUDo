package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Component
@Profile("generateData")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgaCalEventDataGenerator {
    private final OrganisationRepository organisationRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    @PostConstruct
    public void generateData() {
        for (int i = 0; i < 10; i++) {
            Organisation orga = organisationRepository.save(new Organisation("Organisation " + i));
            Calendar calendar = calendarRepository.save(new Calendar("Calendar " + i, Collections.singletonList(orga)));
            for (int j = 0; j < 20; j++) {
                eventRepository.save(new Event(
                    "Event " + j,
                    LocalDateTime.of(2020, 4, (j % 28) + 1, 12, 0),
                    LocalDateTime.of(2020, 4, (j % 28) + 1, 14, 0),
                    calendar
                ));
            }
        }
        organisationRepository.findAll().forEach(System.out::println);
        calendarRepository.findAll().forEach(System.out::println);
        eventRepository.findAll().forEach(System.out::println);

    }
}
