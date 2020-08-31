package at.whattudo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table
@Data
public class AttendanceStatus {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatusPossibilities status;

    @Column(nullable = false)
    private LocalDateTime lastModified;

    public AttendanceStatus() {
    }

    public AttendanceStatus(ApplicationUser user, Event event, AttendanceStatusPossibilities status) {
        this.user = user;
        this.event = event;
        this.status = status;
        this.lastModified = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public AttendanceStatusPossibilities getStatus() {
        return status;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }
}

