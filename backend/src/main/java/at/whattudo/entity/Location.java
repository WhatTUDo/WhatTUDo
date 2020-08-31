package at.whattudo.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity {
    @Id
    @Column
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String address;

    @NonNull
    @Column(nullable = false)
    private String zip;

    @NonNull
    @Column(nullable = false)
    private double latitude;

    @NonNull
    @Column(nullable = false)
    private double longitude;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "location_event",
        joinColumns = @JoinColumn(name = "location_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();

}

