import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {Router} from '@angular/router';
import {
  faBookmark,
  faChevronLeft,
  faCircleNotch,
  faCog,
  faLink,
  faPlus,
  faTimesCircle
} from "@fortawesome/free-solid-svg-icons";
import {OrganizationService} from '../../services/organization.service';
import {AuthService} from "../../services/auth.service";
import {FeedbackService} from "../../services/feedback.service";
import {SubscriptionService} from "../../services/subscription.service";
import {Organization} from '../../dtos/organization';
import {Globals} from "../../global/globals";
import {ICalService} from "../../services/ical.service";


@Component({
  selector: 'app-calendar-search',
  templateUrl: './calendar-search.component.html',
  styleUrls: ['./calendar-search.component.scss']
})
export class CalendarSearchComponent implements OnInit {
  organizationsMap: Map<number, Organization> = new Map();
  searchForm = new FormGroup({
    name: new FormControl('')
  });
  faChevronLeft = faChevronLeft;
  faTimesCircle = faTimesCircle;
  faCog = faCog;
  faPlus = faPlus;
  faBookmark = faBookmark;
  faCircleNotch = faCircleNotch;
  faLink = faLink;

  loading: boolean = true;

  subscribedCalendarIds: number[];
  subscribedCalendars: Calendar[] = [];
  managedCalendars: Calendar[] = [];
  otherCalendars: Calendar[] = [];
  calendarSearchResult: Calendar[] = [];
  searchActive: boolean = false;

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService,
    private subscriptionService: SubscriptionService,
    private feedbackService: FeedbackService,
    private iCalService: ICalService,
    public authService: AuthService,
    public globals: Globals) {
    this.getAllCalendars().then((calendars) => {
      this.loadSubscriptions().then(_ => {
        this.categorizeCalendars(calendars)
      })
    }).finally(() => {
      this.loading = false;
    });
  }

  ngOnInit(): void {
  }

  async getAllCalendars(): Promise<Calendar[]> {
    return await this.calendarService.getAllCalendars().toPromise();
  }

  async loadSubscriptions() {
    if (this.authService.isLoggedIn()) {
      const user = await this.authService.getUser().toPromise();
      const subscribedCalendars = await this.subscriptionService.getSubscribedCalendars(user.id).toPromise();
      this.subscribedCalendarIds = subscribedCalendars.map((cal) => cal.id);
    }
  }

  categorizeCalendars(calendars: Calendar[]) {
    this.subscribedCalendars = calendars.filter(cal => this.subscribedCalendarIds.find(sId => cal.id === sId));
    this.managedCalendars = calendars.filter(
      cal => {
        return (cal.canEdit || cal.canDelete) && !this.subscribedCalendars.find(sc => sc.id === cal.id);
      }
    );
    this.otherCalendars = calendars.filter(
      cal => {
        return !(cal.canEdit || cal.canDelete) && !this.subscribedCalendars.find(sc => sc.id === cal.id);
      }
    )
  }

  updateSubscription() {
    this.loadSubscriptions().then(_ => {
      this.categorizeCalendars(this.subscribedCalendars.concat(this.managedCalendars).concat(this.otherCalendars));
    })
  }

  onSubmitSearch() {
    let formValue = this.searchForm.value;
    if (!formValue.name) {
      this.calendarSearchResult = [];
      this.searchActive = false;
      return
    }
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
      // submit to service
      console.log("search");
      this.calendarService.searchCalendars(formValue.name).subscribe(async (list) => {
        this.calendarSearchResult = list;
        let organizationIdSet = new Set<number>();
        this.calendarSearchResult.forEach(cal => {
          cal.organizationIds.forEach(id => organizationIdSet.add(id));
        })
        for (const id of organizationIdSet) {
          if (!this.organizationsMap.get(id)) {
            this.organizationsMap.set(id, await this.organizationService.getById(id).toPromise())
          }
        }
      });
      this.searchActive = true;
    }
  }


  /**
   *
   * @param formValue
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(formValue: any) {
    let errors: Array<Error> = new Array<Error>();
    if ((formValue.name == "")) {
      errors.push(new Error("Nothing was given to search."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage);
      this.calendarSearchResult = [];
      return false;
    }
    return true;
  }

  clearSearch() {
    this.searchForm = new FormGroup({
      name: new FormControl('')
    });
    this.calendarSearchResult = [];
    this.searchActive = false;
  }

  copyPersonalUrlToClipboard() {
    this.iCalService.getUserIcalToken().subscribe(icalToken => {
      const icalUrl = this.globals.backendUri + "/ical/" + icalToken + "/user.ics";
      this.copyMessage(icalUrl)
      this.feedbackService.displaySuccess("Copied the URL to the clipboard", icalUrl);
    })
  }

  copyCalendarUrlToClipboard(calendarId: number) {
    const icalUrl = this.globals.backendUri + "/ical/" + calendarId + "/calendar.ics";
    this.copyMessage(icalUrl)
    this.feedbackService.displaySuccess("Copied the URL to the clipboard", icalUrl);
  }

  copyAllCalendarsUrlToClipboard() {
    const icalUrl = this.globals.backendUri + "/ical/all-calendars.ics";
    this.copyMessage(icalUrl)
    this.feedbackService.displaySuccess("Copied the URL to the clipboard", icalUrl);
  }

  copyMessage(val: string) {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = val;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
  }
}

