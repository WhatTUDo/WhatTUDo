package at.ac.tuwien.sepm.groupphase.backend.auth;

import at.ac.tuwien.sepm.groupphase.backend.auth.authorities.AdminAuthority;
import at.ac.tuwien.sepm.groupphase.backend.auth.authorities.MemberAuthority;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
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
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) return true;

        if (targetDomainObject instanceof BaseDto) {
            return hasPermission(authentication, (BaseDto) targetDomainObject, permission);
        } else if (targetDomainObject instanceof BaseEntity) {
            return hasPermission(authentication, (BaseEntity) targetDomainObject, permission);
        } else {
            return false;
        }
    }

    public boolean hasPermission(Authentication authentication, BaseDto dto, Object permission) {
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) return true;

        if (dto instanceof OrganizationDto) {
            return evaluatePermission(authentication, organizationRepository.findById(((OrganizationDto) dto).getId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
        } else if (dto instanceof CalendarCreateDto) {
            return evaluatePermission(authentication, organizationRepository.findById(((CalendarCreateDto) dto).getOrgaId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
        } else if (dto instanceof CalendarDto) {
            return evaluatePermission(authentication, calendarRepository.findById(((CalendarDto) dto).getId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
        } else if (dto instanceof EventDto) {
            return evaluatePermission(authentication, calendarRepository.findById(((EventDto) dto).getCalendarId()).orElseThrow(NotFoundException::new), OrganizationRole.valueOf((String) permission));
        } else {
            return false;
        }
    }

    public boolean hasPermission(Authentication authentication, BaseEntity entity, Object permission) {
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) return true;

        if (entity instanceof Organization) {
            return evaluatePermission(authentication, (Organization) entity, OrganizationRole.valueOf((String) permission));
        } else if (entity instanceof Calendar) {
            return evaluatePermission(authentication, (Calendar) entity, OrganizationRole.valueOf((String) permission));
        } else if (entity instanceof Event) {
            return evaluatePermission(authentication, (Event) entity, OrganizationRole.valueOf((String) permission));
        } else {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication.getAuthorities().contains(AdminAuthority.ADMIN)) return true;

        OrganizationRole role = OrganizationRole.valueOf((String) permission);
        switch (targetType) {
            case "ORGA":
                return evaluatePermission(authentication, organizationRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
            case "CAL":
                return evaluatePermission(authentication, calendarRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
            case "EVENT":
                return evaluatePermission(authentication, eventRepository.findById((Integer) targetId).orElseThrow(NotFoundException::new), role);
        }
        return false;
    }

    boolean evaluatePermission(Authentication authentication, Organization organization, OrganizationRole role) {
        return authentication.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals(MemberAuthority.of(organization, role).getAuthority()));
    }

    boolean evaluatePermission(Authentication authentication, Calendar calendar, OrganizationRole role) {
        return calendar.getOrganizations().stream()
            .anyMatch(orga -> authentication.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals(MemberAuthority.of(orga, role).getAuthority())));
    }

    boolean evaluatePermission(Authentication authentication, Event event, OrganizationRole role) {
        return event.getCalendar().getOrganizations().stream()
            .anyMatch(orga -> authentication.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals(MemberAuthority.of(orga, role).getAuthority())));
    }
}
