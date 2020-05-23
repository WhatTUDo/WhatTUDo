package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ORGA_MEMBERSHIP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMembership {
    @EmbeddedId
    private OrganizationMembershipKey id;

    @ManyToOne
    @MapsId("organization_id")
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private OrganizationRole role;
}
