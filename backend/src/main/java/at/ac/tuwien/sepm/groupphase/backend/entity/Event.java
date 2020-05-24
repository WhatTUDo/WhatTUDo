package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

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
}

