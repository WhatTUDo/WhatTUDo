import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CalendarService} from '../../services/calendar.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {faChevronLeft} from '@fortawesome/free-solid-svg-icons';
import {OrganizationService} from '../../services/organization.service';
import {MatSelectModule} from '@angular/material/select';
import {FeedbackHandlerComponent} from "../feedback-handler/feedback-handler.component";
import {Calendar} from '../../dtos/calendar';
import {Organization} from '../../dtos/organization';

@Component({
  selector: 'app-calendar-form',
  templateUrl: './calendar-form.component.html',
  styleUrls: ['./calendar-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class CalendarFormComponent implements OnInit {

  calendar: Calendar;

  organizations: Array<Organization>;

  constructor(
    private route: ActivatedRoute,
    private calendarService: CalendarService,
    private organizationService: OrganizationService,
    private location: Location,
    private select: MatSelectModule
  ) {
  }


  ngOnInit(): void {
    this.getCalendar();
    this.getOrganizations();
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
      }, error => {
        FeedbackHandlerComponent.displayError(error.error.status, error.error.message);
      }, () => {
        this.calendarService.updateOrganizations(this.calendar).subscribe(responseCalendar => {
          console.log("Updated Calendar Organizations:", responseCalendar.organizationIds);
        }, error => {
          FeedbackHandlerComponent.displayError(error.error.status, error.error.message);
        }, () => {
          this.goBack();
        });
      });
    }
  }

  goBack(): void {
    this.location.back();
  }

  getOrganizations() {
    this.organizationService.getAll().subscribe( organizations => {
      this.organizations = organizations;
    });
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

  private compare(id1: number, id2: number): boolean {
    console.log("compared" + id1 + " with " +id2);
    return id1 && id2 ? id1 === id2  : false;
  }

  faChevronLeft = faChevronLeft;

}
