import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faChevronRight, faCog, faPlus, faTimes, faTimesCircle} from '@fortawesome/free-solid-svg-icons'
import {Organization} from "../../dtos/organization";
import {ActivatedRoute} from "@angular/router";
import {OrganizationService} from "../../services/organization.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {User} from "../../dtos/user";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  organization: Organization;
  organizationCalendars: Calendar[] = [];
  organizationMembers: User[];
  editableCalendars: Calendar[];
  pickedCalendarId: number;
  calendarAddExpanded: boolean = false;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;
  faTimes = faTimes;
  faCog = faCog;
  faTimesCircle = faTimesCircle;

  constructor(private organizationService: OrganizationService,
              private calendarService: CalendarService,
              private userService: UserService,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrganization(id);
    this.getAllEditableCalendars();
  }

  ngOnInit(): void {
  }

  onSubmitAddCalendar(calId: number) {
    this.addCalendar(calId);
    this.calendarAddExpanded = false;
    delete this.pickedCalendarId;
  }

  addCalendar(calId: number) {
    console.log('Cal ID' + calId);
    this.organizationService.addCalendarToOrga(this.organization.id, calId).subscribe((organization: Organization) => {
      this.organization = organization;
      this.calendarService.getCalendarById(calId).subscribe((cal: Calendar) => {
        this.organizationCalendars.push(cal);
      })
    }, err => {
      alert(err.message);
    });

  }

  selectCalendar(calId: number) {
    this.pickedCalendarId = calId;
  }

  removeCalendar(calId: number) {
    this.organizationService.removeCalendarToOrga(this.organization.id, calId).subscribe((organization: Organization) => {
      this.organization = organization;
      this.organizationCalendars = this.organizationCalendars.filter((cal: Calendar) => {
        return cal.id != calId
      })
    }, err => {
      alert(err.message);
    });
  }

  getAllEditableCalendars() {
    this.calendarService.getAllCalendars().subscribe((calendars: Calendar[]) => {
      this.editableCalendars = calendars;
    }) //FIXME: Make me to fetch only editable calendars.
  }

  /**
   * Loads Organization with ID from Service.
   * @param id
   */
  private loadOrganization(id: number) {
    this.organizationService.getById(id).subscribe((organization: Organization) => {
      this.organization = organization;
      for (let calID of organization.calendarIds) {
        this.calendarService.getCalendarById(calID).subscribe((cal: Calendar) => {
          this.organizationCalendars.push(cal);
        })
        this.organizationService.getMembers(organization.id).subscribe((users: User[]) => {
          this.organizationMembers = users;
        })
      }
    })
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  deleteOrganization(id: number) {
    if (confirm(`You are deleting organization "${this.organization.name}". Are you sure?`)) {
      this.organizationService.deleteOrganization(id).subscribe(organization => {
      })
    }
  }
}
