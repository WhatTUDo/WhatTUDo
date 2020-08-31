package at.whattudo.service;


import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Subscription;
import at.whattudo.exception.NotFoundException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface SubscriptionService {


    /**
     * Creates a new Subscription.
     *
     * @param subscription: Includes a ApplicationUser and a Calendar object (both of which must already be in DB)
     * @return : The saved Subscription Entity object with valid ID.
     * @throws ServiceException  will be thrown if anything goes wrong during saving.
     * @throws NotFoundException will be thrown if either the ApplicationUser or the Calendar are not found in DB.
     */
    Subscription create(Subscription subscription) throws ServiceException, NotFoundException;


    /**
     * Deletes an existing Subscription
     *
     * @param subscription Subscription to be deleted.
     * @return : The deleted Subscription (equals the given Subscription Entity, may be used for cross-checking)
     * @throws ServiceException  will be thrown if anything goes wrong during deletion.
     * @throws NotFoundException will be thrown if either the ApplicationUser or the Calendar are not found in DB, or the Subscription does not exist.
     */
    Subscription delete(Subscription subscription) throws ServiceException, NotFoundException;


    /**
     * Returns a Subscription Entity for a given ID.
     *
     * @param id ID of the Subscription Entity to be returned.
     * @return Subscription Entity.
     * @throws ServiceException  will be thrown if anything goes wrong during DB processing.
     * @throws NotFoundException will be thrown if the Subscription does not exist or was not found.
     */
    Subscription getById(Integer id) throws ServiceException, NotFoundException;


    /**
     * Returns a List of all Subscriptions for a given ApplicationUser Entity.
     *
     * @param user ApplicationUser Entity.
     * @return List of Subscriptions which are associated with the given ApplicationUser Entity.
     * @throws ServiceException will be thrown if anything goes wrong during DB processing.
     */
    List<Subscription> getSubscriptionsByUser(ApplicationUser user) throws ServiceException;


    /**
     * Returns a List of all Subscriptions for a given Calendar Entity.
     *
     * @param calendar Calendar Entity.
     * @return List of Subscriptions which are associated with the given Calendar Entity.
     * @throws ServiceException will be thrown if anything goes wrong during DB processing.
     */
    List<Subscription> getSubscriptionsForCalendar(Calendar calendar) throws ServiceException;


    /**
     * Returns a List of ApplicationUser Entities who are subscribed to a given Calendar Entity.
     *
     * @param calendar Calendar Entity.
     * @return List of ApplicationUser Entities for which Subscriptions to a Calendar exist.
     * @throws ServiceException will be thrown if anything goes wrong during DB processing.
     */
    List<ApplicationUser> getSubscribedUsersForCalendar(Calendar calendar) throws ServiceException;


    /**
     * Returns a List of Calendar Entities to which an ApplicationUser is subscribed.
     *
     * @param user ApplicationUser.
     * @return List of Calendar Entities to which an ApplicationUser is ubscribed.
     * @throws ServiceException will be thrown if anything goes wrong during DB processing.
     */
    List<Calendar> getSubsribedCalendarsForUser(ApplicationUser user) throws ServiceException;

}
