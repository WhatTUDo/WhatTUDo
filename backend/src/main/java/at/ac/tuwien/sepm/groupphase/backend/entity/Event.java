package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    @ToString.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToMany(mappedBy = "events")
    private List<Label> labels;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<AttendanceStatus> attendanceStatuses;

    @ToString.Exclude
    @Lob
    private Byte[] coverImage;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Nullable
    private String description;

    public Event(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Calendar calendar, Location location) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.calendar = calendar;
        this.location = location;
    }

    public Event(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Calendar calendar, Location location, String description) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.calendar = calendar;
        this.location = location;
        this.description = description;
    }


    public Event() {
    }
}

