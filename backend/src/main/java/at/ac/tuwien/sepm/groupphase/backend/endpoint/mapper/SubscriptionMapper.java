package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public class SubscriptionMapper {
    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CalendarRepository calendarRepository;
}
