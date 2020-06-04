package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.auth.authorities.AdminAuthority;
import at.ac.tuwien.sepm.groupphase.backend.auth.CustomUserDetails;
import at.ac.tuwien.sepm.groupphase.backend.auth.authorities.MemberAuthority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationUser extends BaseEntity implements CustomUserDetails {
    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "password")
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER) // Eager necessary because of getAuthorities()
    private Set<OrganizationMembership> memberships = new HashSet<>();

    @Column(name = "is_sysadmin")
    private boolean isSysAdmin = false;

    /**
     * Infers the User Roles (authorities) from the admin status and the memberships
     * @return a list of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (isSysAdmin) authorities.add(AdminAuthority.ADMIN);
        for (OrganizationMembership membership : memberships) {
            switch (membership.getRole()) {
                case ADMIN:
                    authorities.add(MemberAuthority.of(membership.getOrganization(), OrganizationRole.ADMIN));
                case MOD:
                    authorities.add(MemberAuthority.of(membership.getOrganization(), OrganizationRole.MOD));
                case MEMBER:
                    authorities.add(MemberAuthority.of(membership.getOrganization(), OrganizationRole.MEMBER));
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
