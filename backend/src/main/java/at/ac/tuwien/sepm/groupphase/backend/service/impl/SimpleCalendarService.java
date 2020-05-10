package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.events.calendar.CalendarFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.events.event.EventFindEvent;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleCalendarService implements CalendarService {
    private final ApplicationEventPublisher publisher;
    private final CalendarRepository calendarRepository;

    @Override
    public Calendar findById(Integer id) {
        Optional<Calendar> found = calendarRepository.findById(id);
        if (found.isPresent()) {
            Calendar calendar = found.get();
            publisher.publishEvent(new CalendarFindEvent(calendar.getName()));
            return calendar;
        } else {
            throw new NotFoundException("No event found with id " + id);
        }
    }
}
