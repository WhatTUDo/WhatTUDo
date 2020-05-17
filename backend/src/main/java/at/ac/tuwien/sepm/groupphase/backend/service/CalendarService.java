package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import java.util.List;

public interface CalendarService {

    Calendar findById(Integer id);
    List<Calendar> findAll();
    Calendar save(Calendar calendar);
    List<Calendar> findByName(String name);
}
