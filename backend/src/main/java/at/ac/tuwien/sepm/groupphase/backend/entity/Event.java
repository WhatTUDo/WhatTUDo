package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
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
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<AttendanceStatus> attendanceStatuses;

}

