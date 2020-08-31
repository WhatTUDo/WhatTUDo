package at.whattudo.repository;

import at.whattudo.entity.ApplicationUser;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    /**
     * returns Subscriptions for User
     *
     * @param user
     * @return Subscriptions for User
     */
    List<Subscription> getByUser(ApplicationUser user);


    /**
     * returns Subscriptions for Calendar
     * @param calendar
     * @return Subscriptions for Calendar
     */
    List<Subscription> getByCalendar(Calendar calendar);
}
