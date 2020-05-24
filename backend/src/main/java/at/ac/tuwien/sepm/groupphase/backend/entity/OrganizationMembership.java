package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ORGA_MEMBERSHIP")
@Data
public class OrganizationMembership {
    @EmbeddedId
    private OrganizationMembershipKey id;

    @NonNull
    @ManyToOne
    @MapsId("organization_id")
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @NonNull
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    @NonNull
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private OrganizationRole role;

    public OrganizationMembership() {}

    public OrganizationMembership(Organization organization, ApplicationUser user, OrganizationRole role) {
        this.id = new OrganizationMembershipKey(organization.getId(), user.getId());
        this.organization = organization;
        this.user = user;
        this.role = role;
    }
}
