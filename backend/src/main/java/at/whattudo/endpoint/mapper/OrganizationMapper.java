package at.whattudo.endpoint.mapper;

import at.whattudo.endpoint.dto.OrganizationDto;
import at.whattudo.entity.Calendar;
import at.whattudo.entity.Organization;
import at.whattudo.exception.NotFoundException;
import at.whattudo.repository.CalendarRepository;
import at.whattudo.repository.OrganizationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class OrganizationMapper {
    @Autowired
    protected CalendarRepository calendarRepository;
    @Autowired
    protected OrganizationRepository organizationRepository;
    @Autowired
    protected PermissionEvaluator permissionEvaluator;


    @BeforeMapping
    public void mapCalendars(Organization organization, @MappingTarget OrganizationDto organizationDto) {
        Organization orgEntity = organizationRepository.findById(organization.getId())
            .orElseThrow(() -> new NotFoundException("This organization does not exist in the database"));

        organizationDto.setCalendarIds(orgEntity.getCalendars().stream().map(Calendar::getId).collect(Collectors.toList()));
    }

    @BeforeMapping
    protected void mapPermissions(Organization organization, @MappingTarget OrganizationDto organizationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        organizationDto.setCanEdit(permissionEvaluator.hasPermission(authentication, organization, "ADMIN"));
        organizationDto.setCanDelete(permissionEvaluator.hasPermission(authentication, organization, "ADMIN"));
        organizationDto.setCanCreateCalendar(permissionEvaluator.hasPermission(authentication, organization, "MOD"));
    }

    public abstract OrganizationDto organizationToOrganizationDto(Organization organization);


    @BeforeMapping
    public void mapCalendars(OrganizationDto organizationDto, @MappingTarget Organization organization) {
        organization.setCalendars(calendarRepository.findAllById(organizationDto.getCalendarIds()));
    }

    public abstract Organization organizationDtoToOrganization(OrganizationDto organizationDto);



    /*CalendarService calendarService;

    @Autowired
    public OrganizationMapper(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Organization dtoToEntity(OrganizationDto organizationDto) {

       *//* List<Calendar> calendarList = Collections.emptyList();
        for (int calendar : organizationDto.getCalendarIds()
        ) {
            calendarList.add(calendarService.findById(calendar));
        }
        *//*
        return new Organization(organizationDto.getName());
    }

    public OrganizationDto entityToDto(Organization organization) {
        List<Integer> calendarIds = Collections.emptyList();
        for (Calendar calendar : organization.getCalendars()
        ) {
            calendarIds.add(calendar.getId());
        }
        return new OrganizationDto(organization.getId(), organization.getName(), calendarIds);
    }*/

    public abstract List<OrganizationDto> organizationListToorganizationDtoList(List<Organization> organizationList);

}
