package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Organization {
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
        name = "organization_calendar",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    private List<Calendar> calendars = new ArrayList<>(); //FIXME: Use *Set*. Currently returning duplicates.

    @ToString.Exclude
    @OneToMany(mappedBy = "organization", cascade = {CascadeType.MERGE})
    private Set<OrganizationMembership> memberships;
}

