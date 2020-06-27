import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Calendar} from "../../dtos/calendar";
import {CalendarService} from "../../services/calendar.service";
import {Router} from "@angular/router";
import {OrganizationService} from "../../services/organization.service";
import {SubscriptionService} from "../../services/subscription.service";
import {FeedbackService} from "../../services/feedback.service";
import {faBookmark, faChevronLeft, faCog, faPlus, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {AuthService} from "../../services/auth.service";
import {Globals} from "../../global/globals";
import {Organization} from "../../dtos/organization";
import {SubscriptionDto} from "../../dtos/subscriptionDto";

@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit, OnChanges {
  @Input() calendars: Calendar[]

  /* Event emitted when subscription changed */
  @Output() change = new EventEmitter<number[]>();

  subscribedCalendarIds: number[]

  organizationsMap: Map<number, Organization> = new Map();

  /* color classes to add */
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];

  faChevronLeft = faChevronLeft;
  faTimesCircle = faTimesCircle;
  faCog = faCog;
  faPlus = faPlus;
  faBookmark = faBookmark;

  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService,
    private subscriptionService: SubscriptionService,
    private feedbackService: FeedbackService,
    public authService: AuthService,
    public globals: Globals
  ) {
  }

  ngOnInit(): void {
    this.loadSubscriptions().then()
    this.loadAssociatedOrganization().then()
  }

  ngOnChanges(changes: SimpleChanges) {
    this.loadSubscriptions().then()
    this.loadAssociatedOrganization().then()
  }

  async loadSubscriptions() {
    if (this.authService.isLoggedIn()) {
      const user = await this.authService.getUser().toPromise();
      const subscribedCalendars = await this.subscriptionService.getSubscribedCalendars(user.id).toPromise();
      this.subscribedCalendarIds = subscribedCalendars.map((cal) => cal.id);
    }
  }

  async loadAssociatedOrganization() {
    let organizationIdSet = new Set<number>();
    this.calendars.forEach(cal => {
      cal.organizationIds.forEach(id => organizationIdSet.add(id));
    })
    for (const id of organizationIdSet) {
      let org = await this.organizationService.getById(id).toPromise();
      this.organizationsMap.set(id, org)
    }
  }

  getCalendarColor(calendarId: number) {
    return this.calendarColors[calendarId % this.calendarColors.length];
  }

  isSubscribed(id: number) {
    return Boolean(this.subscribedCalendarIds.find(sId => sId === id));
  }

  //Subscription stuff

  onClickSubscribe(calendarId: number) {
    this.authService.getUser().subscribe(user => {
      let subscription = new SubscriptionDto(0, user.name, calendarId);
      this.subscriptionService.create(subscription).subscribe(savedSub => {
        if (savedSub.calendarId != 0 && savedSub.userName != null) {
          this.feedbackService.displaySuccess("Subscribed!", "You subscribed successfully to this calendar!");
          this.subscribedCalendarIds.push(calendarId);
          this.change.emit(this.subscribedCalendarIds);
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
          this.subscriptionService.delete(filteredSubscriptions.pop().id).subscribe(_ => {
            this.feedbackService.displaySuccess("Unsubscribed!", "You successfully unsubscribed from this calendar!");
            this.subscribedCalendarIds = this.subscribedCalendarIds.filter(cId => cId !== calendarId);
            this.change.emit(this.subscribedCalendarIds);
          })
        } else {
          this.feedbackService.displayError("Subscription Error!", "Could not find a unique Subscription!");
        }
      })
    })
  }

  delete(id: number): void {
    if (confirm(`You are deleting calendar "${this.calendars.find(c => c.id === id).name}". Are you sure?`)) {
      this.calendarService.deleteCalendar({id} as Calendar).subscribe(() => {
        this.calendars = this.calendars.filter(c => c.id !== id);
      });
    }
  }
}
