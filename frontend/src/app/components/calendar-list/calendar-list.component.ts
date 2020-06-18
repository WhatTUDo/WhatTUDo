import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Calendar} from '../../dtos/calendar';
import {Router} from '@angular/router';
import {faChevronLeft, faCog, faTimesCircle, faPlus, faBookmark, faCircleNotch} from "@fortawesome/free-solid-svg-icons";
import {OrganizationService} from '../../services/organization.service';
import {AuthService} from "../../services/auth.service";
import {FeedbackService} from "../../services/feedback.service";
import {SubscriptionService} from "../../services/subscription.service";
import {Organization} from '../../dtos/organization';
import {SubscriptionDto} from "../../dtos/subscriptionDto";


@Component({
  selector: 'app-calendar-list',
  templateUrl: './calendar-list.component.html',
  styleUrls: ['./calendar-list.component.scss']
})
export class CalendarListComponent implements OnInit {
  calendars: Calendar[] = [];
  subscribedCalendarIds: number[];
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

  subscribedCalendars: Calendar[] = [];


  constructor(
    private calendarService: CalendarService,
    private router: Router,
    private organizationService: OrganizationService,
    private subscriptionService: SubscriptionService,
    private feedbackService: FeedbackService,
    public authService: AuthService) {
    this.getAllCalendars().then();

  }

  async getAllCalendars() {
    this.calendars = await this.calendarService.getAllCalendars().toPromise();
    //this.subscribedCalendarIds = this.calendars.map(c => c.id); // FIXME: Delete this line when we actually load sub status.
    this.subscribedCalendarIds = [];
    let organizationIdSet = new Set<number>();
    this.calendars.forEach(cal => {
      cal.organizationIds.forEach(id => organizationIdSet.add(id));
    })
    for (const id of organizationIdSet) {
      this.organizationsMap.set(id, await this.organizationService.getById(id).toPromise())
    }
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe(user => {
        this.subscriptionService.getSubscribedCalendars(user.id).subscribe(calendars => {
          this.subscribedCalendars = calendars;
        })
      })
    }
  }

  delete(id: number): void {
    if (confirm(`You are deleting calendar "${this.calendars.find(c => c.id === id).name}". Are you sure?`)) {
      this.calendarService.deleteCalendar({id} as Calendar).subscribe(() => {
        this.getAllCalendars();
      });
    }
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    return this.organizationService.getOrganizationAvatarLink(organizationId, size);
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

        }
      })
    })
  }

  getOtherCalendars() {
    let calenders = this.calendars.filter(
      cal => !this.subscribedCalendarIds.find(id => id === cal.id)
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


