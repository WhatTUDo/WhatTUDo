import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CalendarService} from '../../services/calendar.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {faChevronLeft} from '@fortawesome/free-solid-svg-icons';
import {OrganizationService} from '../../services/organization.service';
import {Calendar} from '../../dtos/calendar';
import {Organization} from '../../dtos/organization';
import {observable} from "rxjs";
import {CreateCalendar} from "../../dtos/CreateCalendar";
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-calendar-form',
  templateUrl: './calendar-form.component.html',
  styleUrls: ['./calendar-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class CalendarFormComponent implements OnInit {

  calendar: Calendar;

  isUpdate = true;

  organizations: Array<Organization>;
  faChevronLeft = faChevronLeft;
  private selectedImage: File;

  constructor(
    private route: ActivatedRoute,
    private calendarService: CalendarService,
    private organizationService: OrganizationService,
    private location: Location,
    public globals: Globals,
    public router: Router
  ) {
  }

  ngOnInit(): void {
    this.getCalendar();
    this.getOrganizations();
  }

  getCalendar(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id != 0) {
      this.isUpdate = true;
      this.calendarService.getCalendarById(id)
        .subscribe(calendar => this.calendar = calendar);
    } else {
      this.isUpdate = false;
      this.calendar = new Calendar(0, null, [], []);
    }

  }

  onSubmit(): void {
    if (this.validateFormData(this.calendar)) {
      if (this.isUpdate) {
        this.calendarService.editCalendar(this.calendar).subscribe(observable => {
          console.log("Updated calendar: ", observable);
        }, error => {
        }, () => {
          this.calendarService.updateOrganizations(this.calendar).subscribe(responseCalendar => {
            this.router.navigate([`/calendar/${this.calendar.id}`])
          });
        });
      } else {
        let createCalendar = new CreateCalendar(this.calendar.name, this.calendar.organizationIds[0]);
        this.calendarService.addCalendar(createCalendar).subscribe((createdCalendar: Calendar) => {
          console.log("Created calendar: ", observable);
          this.calendar = createdCalendar;
          this.calendarService.updateOrganizations(this.calendar).subscribe(responseCalendar => {
              console.log("Updated Calendar Organizations:", responseCalendar.organizationIds);
              this.router.navigate([`/calendar/${this.calendar.id}`])
            });
        });
      }

    }
  }

  goBack(): void {
    this.location.back();
  }

  getOrganizations() {
    this.organizationService.getAll().subscribe(organizations => {
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
    console.log("compared" + id1 + " with " + id2);
    return id1 && id2 ? id1 === id2 : false;
  }

  selectImage(event) {
    this.selectedImage = event.target.files.item(0);
  }

  uploadImage() {
    this.organizationService.uploadOrganizationAvatar(this.calendar.id, this.selectedImage).subscribe(resp => {
      // @ts-ignore
      this.calendar.coverImageUrl = resp.url;
    });
  }

}
