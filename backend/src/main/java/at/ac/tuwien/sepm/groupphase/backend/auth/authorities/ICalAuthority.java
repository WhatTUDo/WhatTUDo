package at.ac.tuwien.sepm.groupphase.backend.auth.authorities;

import org.springframework.security.core.GrantedAuthority;

public class ICalAuthority implements GrantedAuthority {
    public static ICalAuthority ICAL = new ICalAuthority();

    private ICalAuthority() {
    }

    @Override
    public String getAuthority() {
        return "ICAL";
    }
}
