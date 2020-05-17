package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Component
@Profile("generateData")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgaCalEventDataGenerator {
    private final PlatformTransactionManager txManager;
    private final OrganizationRepository organizationRepository;
    private final CalendarRepository calendarRepository;

    @PostConstruct
    public void generateData() {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                log.info("Generating sample data");
                generateOrgaAndCalendar();
            }
        });
    }

    private void generateOrgaAndCalendar() {
        for (int i = 0; i < 10; i++) {
            Organization orga = organizationRepository.save(new Organization("Organization " + i));
            orga.getCalendars().add(new Calendar("Calendar " + i, Collections.singletonList(orga)));
            orga = organizationRepository.save(orga);

            generateEvents(orga.getCalendars().get(0).getId());
        }
    }

    private void generateEvents(Integer calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId).get();
        for (int j = 0; j < 20; j++) {
            Event event = new Event(
                "Event " + j,
                LocalDateTime.of(2020, 4, (j % 28) + 1, 12, 0),
                LocalDateTime.of(2020, 4, (j % 28) + 1, 14, 0),
                calendar
            );
            calendar.getEvents().add(event);
            calendarRepository.save(calendar);
        }
    }
}
