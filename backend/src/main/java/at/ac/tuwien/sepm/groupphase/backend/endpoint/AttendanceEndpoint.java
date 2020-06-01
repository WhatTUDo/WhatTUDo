package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = AttendanceEndpoint.BASE_URL)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AttendanceEndpoint {
    static final String BASE_URL = "/attendance";
    private final AttendanceService attendanceService;

    


}
