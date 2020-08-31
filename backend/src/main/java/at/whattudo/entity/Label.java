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
public class Label extends BaseEntity {
    @Id
    @Column
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String name;


    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "label_event",
        joinColumns = @JoinColumn(name = "label_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();

}

