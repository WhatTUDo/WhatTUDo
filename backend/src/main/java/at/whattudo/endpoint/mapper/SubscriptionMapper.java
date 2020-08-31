package at.whattudo.endpoint.mapper;


import at.whattudo.endpoint.dto.SubscriptionDto;
import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Subscription;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.SubscriptionRepository;
import at.whattudo.repository.UserRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class SubscriptionMapper {
    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CalendarRepository calendarRepository;

    @BeforeMapping
    protected void mapUser(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription) {
        Optional<ApplicationUser> foundUser = userRepository.findByName(subscriptionDto.getUserName());
        if (!foundUser.isPresent()) {
            throw new NotFoundException("User Not Found!");
        }
        subscription.setUser(foundUser.get());
    }

    @BeforeMapping
    protected void mapCalendar(SubscriptionDto subscriptionDto, @MappingTarget Subscription subscription) {
        Optional<Calendar> foundCalendar = calendarRepository.findById(subscriptionDto.getCalendarId());

        if (!foundCalendar.isPresent()) {
            throw new NotFoundException("Calendar not found!");
        }
        subscription.setCalendar(foundCalendar.get());
    }

    public abstract Subscription subscriptionDtoToSubscription(SubscriptionDto subscriptionDto);


    @BeforeMapping
    protected void mapCalendar(Subscription subscription, @MappingTarget SubscriptionDto subscriptionDto) {
        subscriptionDto.setCalendarId(subscription.getCalendar().getId());
    }

    @BeforeMapping
    protected void mapUser(Subscription subscription, @MappingTarget SubscriptionDto subscriptionDto) {
        subscriptionDto.setUserName(subscription.getUser().getName());
    }

    public abstract SubscriptionDto subscriptionToSubscriptionDto(Subscription subscription);

    public abstract List<SubscriptionDto> subscriptionDtoList(List<Subscription> subscriptionList);

}
