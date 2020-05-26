package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
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
    private final OrganizationRepository organizationRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    @PostConstruct
    public void generateData() {
        log.info("Generating sample data");
        for (int i = 0; i < 10; i++) {
            Organization orga = organizationRepository.save(new Organization("Organization " + i));
            Calendar calendar = calendarRepository.save(new Calendar("Calendar " + i, Collections.singletonList(orga)));
            orga.getCalendars().add(calendar);
            organizationRepository.save(orga);

            for (int j = 0; j < 20; j++) {
                Event event = new Event(
                    "Event " + j,
                    LocalDateTime.of(2020, 4, (j % 28) + 1, 12, 0),
                    LocalDateTime.of(2020, 4, (j % 28) + 1, 14, 0),
                    calendar
                );
                eventRepository.save(event);
                calendar.getEvents().add(event);
            }

            calendarRepository.save(calendar);
        }
    }
}
