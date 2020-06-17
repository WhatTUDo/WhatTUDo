package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SubscriptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

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

}
