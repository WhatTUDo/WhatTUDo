package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrganisationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Calendar;
import at.ac.tuwien.sepm.groupphase.backend.entity.Organisation;
import at.ac.tuwien.sepm.groupphase.backend.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrganisationMapper {

    CalendarService calendarService;

    @Autowired
    public OrganisationMapper(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Organisation dtoToEntity(OrganisationDto organisationDto) {

       /* List<Calendar> calendarList = Collections.emptyList();
        for (int calendar : organisationDto.getCalendarIds()
        ) {
            calendarList.add(calendarService.findById(calendar));
        }
        */
        return new Organisation(organisationDto.getName());
    }

    public OrganisationDto entityToDto(Organisation organisation) {
        List<Integer> calendarIds = Collections.emptyList();
        for (Calendar calendar : organisation.getCalendars()
        ) {
            calendarIds.add(calendar.getId());
        }
        return new OrganisationDto(organisation.getId(), organisation.getName(), calendarIds);
    }


}
