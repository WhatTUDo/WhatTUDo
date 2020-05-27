import {Component, OnInit} from '@angular/core';
import {CalendarService} from '../../services/calendar.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";
import {OrganizationService} from "../../services/organization.service";
import {FormControl, FormGroup} from "@angular/forms";
import {Calendar} from "../../dtos/calendar";
import {Organization} from "../../dtos/organization";
import {observable} from "rxjs";

@Component({
  selector: 'app-calendar-form',
  templateUrl: './calendar-form.component.html',
  styleUrls: ['./calendar-form.component.scss']
})

export class CalendarFormComponent implements OnInit {

  calendar: Calendar;

  organizations: Array<Organization>;

  reactiveCalendarForm = new FormGroup( {
    organizationIds: new FormControl(''),
    name: new FormControl('')
  });

  constructor(
    private route: ActivatedRoute,
    private calendarService: CalendarService,
    private organizationService: OrganizationService,
    private location: Location
  ) {
  }


  ngOnInit(): void {
    this.getCalendar();
    this.getOrganizations()
  }

  getCalendar(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.calendarService.getCalendarById(id)
      .subscribe(calendar => this.calendar = calendar);
  }

  onSubmit(): void {
    if (this.validateFormData(this.calendar)) {

      this.calendarService.editCalendar(this.calendar).subscribe( observable => {
        console.log("Updated calendar: ", observable);
        this.goBack();
      })
    }
  }

  goBack(): void {
    this.location.back();
  }

  getOrganizations() {
    this.organizationService.getAll().subscribe( organizations => {
      this.organizations = organizations;
    })
  }

  private validateFormData(calendar: any): boolean {
    let errors = [];

    if (calendar.name == null || calendar.name == "") {
      errors.push("Name must not be null or empty!");
    }

    if (calendar.organizationIds == null || calendar.organizationIds == "") {
      errors.push("Organization must be specified!");
    }

    if (errors.length > 0) {
      let msg = "";
      for (let error of errors) {
        msg += error + "\n ";
      }

      alert(msg);
      return false;
    }
    return true;
  }

  faChevronLeft = faChevronLeft;

}
