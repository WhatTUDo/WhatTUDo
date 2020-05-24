package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import java.util.List;

public interface CalendarService {

    Calendar findById(Integer id);
    List<Calendar> findAll();

    /**
     * @param calendar to be saved into database .
     * @return the new calendar.
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if.
     *         - name of calendar is empty;
     *
     */
    Calendar save(Calendar calendar);

    List<Calendar> findByName(String name);


    /**
     * @param id of the calendar to be deleted into database .
     * @throws org.hibernate.service.spi.ServiceException will be thrown if something goes wrong during data processing.
     * @throws at.ac.tuwien.sepm.groupphase.backend.util.ValidationException will be thrown if.
     *         - calendar has no id in db
     */
    void delete(Integer id);
}
