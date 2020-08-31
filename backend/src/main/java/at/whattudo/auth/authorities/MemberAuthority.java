package at.whattudo.auth.authorities;

import at.whattudo.entity.Organization;
import at.whattudo.entity.OrganizationRole;
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
