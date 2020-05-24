package at.ac.tuwien.sepm.groupphase.backend.config;

import at.ac.tuwien.sepm.groupphase.backend.config.authorities.AdminAuthority;
import at.ac.tuwien.sepm.groupphase.backend.config.authorities.MemberAuthority;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CalendarDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganizationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.OrganizationRole;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organization;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.Serializable;

@Component
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MemberEvaluator implements PermissionEvaluator {
    private final OrganizationRepository organizationRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) {
            return true;
        } else {
            if (targetDomainObject instanceof OrganizationDto) {
                return hasPermission(authentication, organizationRepository.findById(((OrganizationDto) targetDomainObject).getId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
            } else if (targetDomainObject instanceof CalendarDto) {
                return hasPermission(authentication, calendarRepository.findById(((CalendarDto) targetDomainObject).getId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
            } else if (targetDomainObject instanceof EventDto) {
                return hasPermission(authentication, eventRepository.findById(((EventDto) targetDomainObject).getId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
            }
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) {
            return true;
        } else {
            OrganizationRole role = OrganizationRole.valueOf((String) permission);
            switch (targetType) {
                case "ORGA":
                    return hasPermission(authentication, organizationRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
                case "CAL":
                    return hasPermission(authentication, calendarRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
                case "EVENT":
                    return hasPermission(authentication, eventRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
            }
            return false;
        }
    }

    boolean hasPermission(Authentication authentication, Organization organization, OrganizationRole role) {
        return authentication.getAuthorities().contains(MemberAuthority.of(organization, role));
    }

    boolean hasPermission(Authentication authentication, Calendar calendar, OrganizationRole role) {
        return calendar.getOrganizations().stream()
            .anyMatch(it -> authentication.getAuthorities().contains(MemberAuthority.of(it, role)));
    }

    boolean hasPermission(Authentication authentication, Event event, OrganizationRole role) {
        return event.getCalendar().getOrganizations().stream()
            .anyMatch(it -> authentication.getAuthorities().contains(MemberAuthority.of(it, role)));
    }
}
