package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseEntity {
    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "organization_calendar",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )

    private List<Calendar> calendars = new ArrayList<>(); //FIXME: Use *Set*. Currently returning duplicates.

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "organization", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<OrganizationMembership> memberships = new HashSet<>();

    @ToString.Exclude
    @Lob
    private Byte[] coverImage;
}

