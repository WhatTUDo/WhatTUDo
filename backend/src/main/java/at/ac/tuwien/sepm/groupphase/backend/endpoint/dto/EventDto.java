package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;

import java.time.LocalDateTime;


//TODO: annotations

public class EventDto {

    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int id;
    private Calendar calendar;

    public EventDto(String name, LocalDateTime startDate, LocalDateTime endDate, Calendar calendar, int id) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calendar = calendar;
        this.id = id;
    }

    public EventDto(String name, LocalDateTime startDate, LocalDateTime endDate, Calendar calendar) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getId() { return id; }

    public Calendar getCalendar() { return calendar; }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
}
