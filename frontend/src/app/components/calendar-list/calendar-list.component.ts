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
  faPlus,
  faTimesCircle,
  faFileDownload
} from "@fortawesome/free-solid-svg-icons";
import {OrganizationService} from '../../services/organization.service';
import {AuthService} from "../../services/auth.service";
import {FeedbackService} from "../../services/feedback.service";
import {SubscriptionService} from "../../services/subscription.service";
import {Organization} from '../../dtos/organization';
import {SubscriptionDto} from "../../dtos/subscriptionDto";
import {Globals} from "../../global/globals";


@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit {
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
  faFileDownload = faFileDownload;

  loading: boolean = true;

  subscribedCalendars: Calendar[] = [];
  managedCalendars: Calendar[] = [];
  otherCalendars: Calendar[] = [];
  calendarSearchResult: Calendar[] = [];
  searchActive: boolean = false;

  //TODO: Get filter form out of commit history.

  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService,
    private subscriptionService: SubscriptionService,
    private feedbackService: FeedbackService,
    public authService: AuthService,
    private globals: Globals) {
    this.getAllCalendars().then((calendars) => {
      this.loadSubscriptions().then(() => {
        this.managedCalendars = calendars.filter(
          cal => {
            return (cal.canEdit || cal.canDelete) && !this.subscribedCalendars.find(sc => sc.id === cal.id);
          }
        );
      })
      this.otherCalendars = calendars.filter(
        cal => {
          return !(cal.canEdit || cal.canDelete) && !this.subscribedCalendars.find(sc => sc.id === cal.id);
        }
      )
    }).finally(() => {
      this.loading = false;
    });

  }

  async getAllCalendars(): Promise<Calendar[]> {
    let calendars: Calendar[] = await this.calendarService.getAllCalendars().toPromise();
    let organizationIdSet = new Set<number>();
    calendars.forEach(cal => {
      cal.organizationIds.forEach(id => organizationIdSet.add(id));
    })
    for (const id of organizationIdSet) {
      let org = await this.organizationService.getById(id).toPromise();
      org.coverImageUrl = this.globals.backendUri + org.coverImageUrl.slice(1);
      this.organizationsMap.set(id, org)
    }
    return calendars;
  }

  ngOnInit(): void {
  }

  async loadSubscriptions() {
    if (this.authService.isLoggedIn()) {
      const user = await this.authService.getUser().toPromise();
      this.subscribedCalendars = await this.subscriptionService.getSubscribedCalendars(user.id).toPromise();
    }
  }

  delete(id: number): void {
    if (confirm(`You are deleting calendar "${this.otherCalendars
      .concat(this.managedCalendars).concat(this.subscribedCalendars)
      .find(c => c.id === id).name}". Are you sure?`)) {
      this.calendarService.deleteCalendar({id} as Calendar).subscribe(() => {
        this.getAllCalendars();
      });
    }
  }

  //Subscription stuff

  onClickSubscribe(calendarId: number) {
    this.authService.getUser().subscribe(user => {
      let subscription = new SubscriptionDto(0, user.name, calendarId);
      this.subscriptionService.create(subscription).subscribe(savedSub => {
        if (savedSub.calendarId != 0 && savedSub.userName != null) {
          this.feedbackService.displaySuccess("Subscribed!", "You subscribed successfully to this calendar!");

          let calendar = this.managedCalendars.find(cal => {
            return cal.id === calendarId
          });
          this.managedCalendars = this.managedCalendars.filter(cal => {
            return cal.id !== calendarId
          });
          if (!calendar) {
            calendar = this.otherCalendars.find(cal => {
              return cal.id === calendarId
            });
            this.otherCalendars = this.otherCalendars.filter(cal => {
              return cal.id !== calendarId
            });
          }
          this.subscribedCalendars.push(calendar);
        }
      })
    })
  }

  onClickUnsubscribe(calendarId: number) {
    this.authService.getUser().subscribe(user => {
      this.subscriptionService.getSubscriptionsForUser(user.id).subscribe(subscriptions => {
        let filteredSubscriptions = subscriptions.filter(sub => {
          return sub.calendarId === calendarId
        });
        if (filteredSubscriptions.length === 1) {
          this.subscriptionService.delete(filteredSubscriptions.pop().id).subscribe(deletedSubscription => {
            this.feedbackService.displaySuccess("Unsubscribed!", "You successfully unsubscribed from this calendar!");
            let calendar = this.subscribedCalendars.find(cal => {
              return cal.id === calendarId
            });
            if (calendar.canEdit || calendar.canDelete) {
              this.managedCalendars.push(calendar);
            } else {
              this.otherCalendars.push(calendar);
            }
            this.subscribedCalendars = this.subscribedCalendars.filter(cal => {
              return cal.id !== calendarId
            });
          })
        } else {
          this.feedbackService.displayError("Subscription Error!", "Could not find a unique Subscription!");
        }
      })
    })
  }

  onSubmit() {
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
    this.calendarSearchResult = [];
    this.searchActive = false;
  }

  isSubscribed(id: number) {
    return Boolean(this.subscribedCalendars.find(sc => sc.id === id));
  }
}

