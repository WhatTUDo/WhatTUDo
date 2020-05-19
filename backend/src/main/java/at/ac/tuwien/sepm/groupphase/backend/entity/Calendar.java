package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Calendar {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    @NonNull
    @ToString.Exclude
    @ManyToMany(mappedBy = "calendars", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @Size(min = 1)
    private List<Organization> organizations;

    @ToString.Exclude
    @OneToMany(mappedBy = "calendar")
    private List<Event> events = new ArrayList<>();
}

