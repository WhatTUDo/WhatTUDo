package at.whattudo.auth.authorities;

import org.springframework.security.core.GrantedAuthority;

public class AdminAuthority implements GrantedAuthority {
    public static AdminAuthority ADMIN = new AdminAuthority();

    private AdminAuthority() {
    }

    @Override
    public String getAuthority() {
        return "ROLE_SYSADMIN";
    }
}
