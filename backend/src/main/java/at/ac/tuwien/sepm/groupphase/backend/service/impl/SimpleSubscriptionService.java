package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SubscriptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SimpleSubscriptionService implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    @Override
    public Subscription create(Subscription subscription) throws ServiceException, NotFoundException {
        try {
            return subscriptionRepository.save(subscription);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Subscription delete(Subscription subscription) throws ServiceException {
        try {
            subscriptionRepository.delete(subscription);
            return subscription;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Subscription> getSubscriptionsByUser(ApplicationUser user) throws ServiceException, NotFoundException {
        try {
            return subscriptionRepository.getByUser(user);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Subscription> getSubscriptionsForCalendar(Calendar calendar) throws ServiceException, NotFoundException {
        try {
            return subscriptionRepository.getByCalendar(calendar);
        }
        catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationUser> getSubscribedUsersForCalendar(Calendar calendar) throws ServiceException, NotFoundException {
        List<Subscription> subscriptions = this.getSubscriptionsForCalendar(calendar);
        List<ApplicationUser> users = new ArrayList<>();

        subscriptions.forEach(subscription -> {
            users.add(subscription.getUser());
        });
        return users;
    }

    @Override
    public List<Calendar> getSubsribedCalendarsForUser(ApplicationUser user) throws ServiceException, NotFoundException {
        List<Subscription> subscriptions = this.getSubscriptionsByUser(user);
        List<Calendar> calendars = new ArrayList<>();
        subscriptions.forEach(subscription -> {
            calendars.add(subscription.getCalendar());
        });
        return calendars;
    }
}
