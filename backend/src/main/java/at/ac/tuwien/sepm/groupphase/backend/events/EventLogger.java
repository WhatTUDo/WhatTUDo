package at.ac.tuwien.sepm.groupphase.backend.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventLogger {
    @EventListener
    public void logEvent(CustomEvent event) {
        log.trace(event.toString());
    }
}
