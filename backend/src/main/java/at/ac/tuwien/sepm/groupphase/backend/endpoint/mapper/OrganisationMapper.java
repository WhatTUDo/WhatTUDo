package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganisationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CalendarRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrganisationRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Transactional
@Mapper(componentModel = "spring")
public abstract class OrganisationMapper {
    @Autowired protected CalendarRepository calendarRepository;
    @Autowired protected OrganisationRepository organisationRepository;

    public abstract OrganisationDto organisationToOrganisationDto(Organisation organisation);

    @BeforeMapping
    public void mapCalendars(Organisation organisation, @MappingTarget OrganisationDto organisationDto) {
        Organisation orgEntity = organisationRepository.findById(organisation.getId())
            .orElseThrow(() -> new NotFoundException("This organisation does not exist in the database"));

        organisationDto.setCalendarIds(orgEntity.getCalendars().stream().map(Calendar::getId).collect(Collectors.toList()));
    }

    public abstract Organisation organisationDtoToOrganisation(OrganisationDto organisationDto);

    @BeforeMapping
    public void mapCalendars(OrganisationDto organisationDto, @MappingTarget Organisation organisation) {
        organisation.setCalendars(calendarRepository.findAllById(organisationDto.getCalendarIds()));
    }
    /*CalendarService calendarService;

    @Autowired
    public OrganisationMapper(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Organisation dtoToEntity(OrganisationDto organisationDto) {

       *//* List<Calendar> calendarList = Collections.emptyList();
        for (int calendar : organisationDto.getCalendarIds()
        ) {
            calendarList.add(calendarService.findById(calendar));
        }
        *//*
        return new Organisation(organisationDto.getName());
    }

    public OrganisationDto entityToDto(Organisation organisation) {
        List<Integer> calendarIds = Collections.emptyList();
        for (Calendar calendar : organisation.getCalendars()
        ) {
            calendarIds.add(calendar.getId());
        }
        return new OrganisationDto(organisation.getId(), organisation.getName(), calendarIds);
    }*/


}
