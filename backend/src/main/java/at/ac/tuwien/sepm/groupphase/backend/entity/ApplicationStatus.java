package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ApplicationStatus<Attendance> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @OneToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @NonNull
    @OneToOne
    @MapsId("event_id")
    @JoinColumn(name = "event_id")
    private Event event;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Column(nullable = false)
    private LocalDateTime lastModified;

}

