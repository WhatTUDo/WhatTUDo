package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Organisation {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "organisation_calendar",
        joinColumns = @JoinColumn(name = "organisation_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    private List<Calendar> calendars = Collections.emptyList();
}

