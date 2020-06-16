package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = SubscriptionEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubscriptionEndpoint {
    static final String BASE_URL = "/subscription";

    private final SubscriptionService subscriptionService;


}
