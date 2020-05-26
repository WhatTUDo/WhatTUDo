package at.ac.tuwien.sepm.groupphase.backend.auth.authorities;

import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@Value(staticConstructor = "of")
public class MemberAuthority implements GrantedAuthority {
    Organization organization;
    OrganizationRole organizationRole;

    @Override
    public String getAuthority() {
        return organizationRole + "_" + organization.getId();
    }
}
