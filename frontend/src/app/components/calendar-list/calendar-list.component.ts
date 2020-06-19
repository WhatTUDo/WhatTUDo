import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {Router} from '@angular/router';
import {
  faChevronLeft,
  faCog,
  faTimesCircle,
  faPlus,
  faBookmark,
  faCircleNotch
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
  otherCalenderFilteredBy: string = "";

  loading: boolean = true;

  subscribedCalendars: Calendar[] = [];
  managedCalendars: Calendar[] = [];
  calendars: Calendar[] = [];

  //TODO: Get filter form out of commit history.

  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService,
    private subscriptionService: SubscriptionService,
    private feedbackService: FeedbackService,
    public authService: AuthService,
    private globals: Globals) {
    this.getAllCalendars().then(() => {
      this.loadSubscriptions().then(() => {
        console.log(this.subscribedCalendars);
        this.managedCalendars = this.calendars.filter(
          cal => {
            return (cal.canEdit || cal.canDelete) && !this.subscribedCalendars.find(sc => sc.id === cal.id);
          }
        );
      }).finally(() => {
        this.loading = false;
      });
    });

  }

  async getAllCalendars() {
    this.calendars = await this.calendarService.getAllCalendars().toPromise();
    let organizationIdSet = new Set<number>();
    this.calendars.forEach(cal => {
      cal.organizationIds.forEach(id => organizationIdSet.add(id));
    })
    for (const id of organizationIdSet) {
      let org = await this.organizationService.getById(id).toPromise();
      org.coverImageUrl = this.globals.backendUri + org.coverImageUrl.slice(1);
      this.organizationsMap.set(id, org)
    }
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
    if (confirm(`You are deleting calendar "${this.calendars.find(c => c.id === id).name}". Are you sure?`)) {
      this.calendarService.deleteCalendar({id} as Calendar).subscribe(() => {
        this.getAllCalendars();
      });
    }
  }

  //Subscription stuff

  onClickSubscribe(calendarId: number) {
    this.authService.getUser().subscribe(user => {
      let userName = user.name;
      let subscription = new SubscriptionDto(0, userName, calendarId);
      this.subscriptionService.create(subscription).subscribe(savedSub => {
        if (savedSub.calendarId != 0 && savedSub.userName != null) {
          this.feedbackService.displaySuccess("Subscribed!", "You subscribed successfully to this calendar!");
          let calendar = this.calendars.filter(cal => {
            return cal.id == calendarId
          }).pop();
          this.subscribedCalendars.push(calendar);
        }
      })
    })
  }

  onClickUnsubscribe(calendarId: number) {
    this.authService.getUser().subscribe(user => {
      this.subscriptionService.getSubscriptionsForUser(user.id).subscribe(subscriptions => {
        let filteredSubscriptions = subscriptions.filter(sub => {
          return sub.calendarId == calendarId
        });
        if (filteredSubscriptions.length == 1) {
          this.subscriptionService.delete(filteredSubscriptions.pop().id).subscribe(deletedSubscription => {
            this.feedbackService.displaySuccess("Unsubscribed!", "You successfully unsubscribed from this calendar!");
            this.subscribedCalendars = this.subscribedCalendars.filter(cal => {
              return cal.id != calendarId
            });
          })
        } else {
          this.feedbackService.displayError("Subscription Error!", "Could not find a unique Subscription!");
        }
      })
    })
  }

  getOtherCalendars() {
    let calenders = this.calendars.filter(
      cal => !this.subscribedCalendars.find(sc => sc.id === cal.id) &&
        !this.managedCalendars.find(mc => mc.id === cal.id)
    )
    if (this.otherCalenderFilteredBy) {
      calenders = calenders.filter(cal => {
        const nameMatched = cal.name.toLowerCase().match(this.otherCalenderFilteredBy.toLowerCase());
        const orgMatched = cal.organizationIds.find(orgId =>
          this.organizationsMap.get(orgId).name.toLowerCase().match(this.otherCalenderFilteredBy.toLowerCase())
        )
        return nameMatched || orgMatched;
      })
    }
    return calenders;
  }

  filter(event: Event) {
    // @ts-ignore
    this.otherCalenderFilteredBy = event.target.value;
  }
}


