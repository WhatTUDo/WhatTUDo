package at.whattudo.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseEntity {
    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
        name = "organization_calendar",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    private List<Calendar> calendars = new ArrayList<>(); //FIXME: Use *Set*. Currently returning duplicates.

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "organization", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OrganizationMembership> memberships = new HashSet<>();

    @ToString.Exclude
    @Lob
    private Byte[] coverImage;

    @Nullable
    private String description;

    public Organization() {
    }

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

