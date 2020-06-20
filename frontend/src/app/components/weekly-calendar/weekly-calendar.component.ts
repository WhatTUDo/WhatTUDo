import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CalendarEvent} from '../../dtos/calendar-event';
import {faChevronDown, faChevronLeft, faChevronRight, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import {EventService} from "../../services/event.service";
import {SubscriptionService} from "../../services/subscription.service";
import {Globals} from "../../global/globals";
import {AuthService} from "../../services/auth.service";
import {CalendarBase} from "../calendar/calendar-base";

@Component({
  selector: 'app-weekly-calendar',
  templateUrl: './weekly-calendar.component.html',
  styleUrls: ['./weekly-calendar.component.scss']
})
export class WeeklyCalendarComponent extends CalendarBase implements OnInit, OnChanges {

  currentDate: number;
  currentMonth: String;
  currentYear: number;

  /** color classes to add **/
  calendarColors = ["blue", "green", "yellow", "orange", "red", "violet"];


  eventsOfTheWeek: Map<String, CalendarEvent[]> = new Map<String, CalendarEvent[]>();


  @Input("subscribedOnly") filterIsActive: boolean;
  subscribeCalendarIds: number[];


  constructor(
    public eventService: EventService,
    public globals: Globals,
    private subscriptionService: SubscriptionService,
    private authService: AuthService,
  ) {
    super(eventService, globals);
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe(user => {
        this.subscriptionService.getSubscribedCalendars(user.id).subscribe(calendars => {
          this.subscribeCalendarIds = calendars.map(cal => {
            return cal.id
          });
        })
      })
    }

    this.loadEvents();
    this.updateDatetime();
    setInterval(_ => {
      this.updateDatetime();
    }, 1000);
  }


  ngOnChanges(changes: SimpleChanges) {
    this.loadEvents();
  }

  loadEvents() {
    if (this.filterIsActive) {
      let filteredEventsOfTheWeek = new Map<String, CalendarEvent[]>();
      this.eventsOfTheWeek.forEach((events, day) => {
        let filteredEvents = events.filter(event => {
          return this.subscribeCalendarIds.includes(event.calendarId)
        });
        filteredEventsOfTheWeek.set(day, filteredEvents);
      });
      this.eventsOfTheWeek = filteredEventsOfTheWeek;
    } else {
      this.loadAllEventsForWeek(this.displayingWeek[0], this.displayingWeek[6]);
    }
  }

  /**
   * Calls Service to load Events for the week.
   * @param from: Start date of week
   * @param to: End date of week
   */
  loadAllEventsForWeek(from: Date, to: Date) {
    this.eventService.getMultipleEvents(from, to).subscribe((events: Array<CalendarEvent>) => {
      events.forEach(event => {
        let startDate = new Date(event.startDateTime)
        let endDate = new Date(event.endDateTime);

        event.startDateTime = startDate;
        event.endDateTime = endDate;
      });
      this.displayingWeek.forEach((day: Date) => {
        let keyISOString = this.getMidnight(day).toISOString()
        this.eventsOfTheWeek.set(keyISOString, events.filter(event => {
          const isAfterMidnight = event.endDateTime.getTime() > this.getMidnight(day).getTime();
          const isBeforeEndOfDay = event.startDateTime.getTime() < this.getEndOfDay(day).getTime();
          return isAfterMidnight && isBeforeEndOfDay;
        }));
      })
    });
  }

  updateDatetime() {
    const today = this.getToday();
    this.currentMonth = today.toLocaleString(this.globals.dateLocale, {month: 'long'});
    this.currentDate = today.getDate();
    this.currentYear = today.getFullYear();
  }

  getCalendarColor(event: CalendarEvent) {
    return this.calendarColors[event.calendarId % this.calendarColors.length];
  }
}
