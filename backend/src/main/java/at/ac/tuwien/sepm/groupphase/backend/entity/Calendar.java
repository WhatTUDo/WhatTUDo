package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table
@Data
//@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Calendar extends BaseEntity {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @ToString.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToMany(mappedBy = "calendars", fetch = FetchType.EAGER)
    @Size(min = 1)
    private List<Organization> organizations;

    @ToString.Exclude
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.MERGE)
    private List<Event> events = new ArrayList<>();

    @Nullable
    private String description;

    @ToString.Exclude
    @Lob
    private Byte[] coverImage;

    public Calendar(String name, List<Organization> organizations, String description) {
        this.name = name;
        this.organizations = organizations;
        this.description = description;
    }

    public Calendar() {
    }
}

