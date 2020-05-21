import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faCog, faPlus, faTimesCircle} from '@fortawesome/free-solid-svg-icons'
import {Organization} from "../../dtos/organization";
import {ActivatedRoute} from "@angular/router";
import {OrganizationService} from "../../services/organization.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  organization: Organization;
  organizationCalenders: Calendar[] = [];
  // organization = new Organization(null, "FS Winf", []);
  // organizationMembers: OrganizationMemberOrSomething[];
  organizationMembers = [
    {username: "JaneDoe", role: "Admin"},
    {username: "JohnyAppleseed", role: "Member"},
  ]

  constructor(private organizationService: OrganizationService, private calendarService: CalendarService,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrganization(id);
  }

  ngOnInit(): void {
  }

  /**
   * Loads Organization with ID from Service.
   * @param id
   */
  private loadOrganization(id: number) {
    this.organizationService.getById(id).subscribe((organization: Organization) => {
      this.organization = organization;
      console.log(organization);
      for (let calID of organization.calendarIds) {
        console.log(calID);
        this.calendarService.getCalendarById(calID).subscribe((cal: Calendar) => {
          this.organizationCalenders.push(cal);

        }, err => {
          alert(err.message);
        })
      }
    }, err => {
      alert(err.message);
    })
  }

  // deleteOrganization() {
  // }

  faChevronLeft = faChevronLeft;
  faPlus = faPlus;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
}
