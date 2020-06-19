import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from "../../services/user.service";
import {Globals} from "../../global/globals";
import {EventService} from "../../services/event.service";
import {CalendarEvent} from "../../dtos/calendar-event";
import {falseIfMissing} from 'protractor/built/util';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentTime: string;
  userId: number;
  recommendedEvents: CalendarEvent[];
  subscribedOnly: boolean= false;
  view: boolean[] = [false, false, false, true];
  constructor(public authService: AuthService,
              private userService: UserService,
              private eventService: EventService,
              private globals: Globals
  ) {
  }

  ngOnInit() {
    this.loadRecommendedEvents();
    this.updateDatetime();
    setInterval(() => {
      this.updateDatetime();
    }, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentTime = date.toLocaleString(this.globals.dateLocale, {hour: 'numeric', minute: 'numeric'})
  }


  loadRecommendedEvents() {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user) => {
        this.userId = user.id;
        this.userService.getRecommendedEvents(user.id).subscribe((events: CalendarEvent[]) => {
          this.recommendedEvents = events;
        })
      })
    } else {
      this.eventService.getMultipleEvents(
        new Date(Date.now()),
        new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
      ).subscribe((events: CalendarEvent[]) => {
        this.recommendedEvents = events.sort((a, b) => {
          return a.id - b.id; // TODO: Make comparison based on #Attendees.
        }).slice(0, 5);
      })
    }
  }

  toggleSubscribedOnly() {
    this.subscribedOnly = !this.subscribedOnly;
  }

  chooseView(viewNr: number){
    if(viewNr == 0){
      this.view = [true, false, false, false];
    }else if(viewNr == 1) {
      this.view = [false, true, false, false];
    }else if(viewNr == 2){
      this.view= [false, false, true , false];
    }else if(viewNr == 3){
      this.view = [false, false, false, true];
    }
  }


}
