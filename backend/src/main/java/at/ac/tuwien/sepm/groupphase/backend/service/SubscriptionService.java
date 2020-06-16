package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Subscription;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface SubscriptionService {


    //TODO: Write doc.
    Subscription create(Subscription subscription) throws ServiceException, NotFoundException;

    List<Subscription> getSubscriptionsByUser(ApplicationUser user) throws ServiceException, NotFoundException;

    List<Subscription> getSubscriptionsForCalendar(Calendar calendar) throws ServiceException, NotFoundException;

    List<ApplicationUser> getSubscribedUsersForCalendar(Calendar calendar) throws ServiceException, NotFoundException;

    List<Calendar> getSubsribedCalendarsForUser(ApplicationUser user) throws ServiceException, NotFoundException;

}
