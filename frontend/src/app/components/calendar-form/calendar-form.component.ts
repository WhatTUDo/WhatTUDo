import {Component, OnInit} from '@angular/core';
import {CalendarService} from '../../services/calendar.service';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-calendar-form',
  templateUrl: './calendar-form.component.html',
  styleUrls: ['./calendar-form.component.scss']
})

export class CalendarFormComponent implements OnInit {

  calendar;

  constructor(
    private route: ActivatedRoute,
    private calendarService: CalendarService,
    private location: Location
  ) {
  }


  ngOnInit(): void {
    this.getCalendar();
  }

  getCalendar(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.calendarService.getCalendarById(id)
      .subscribe(calendar => this.calendar = calendar);
  }

  update(): void {
    this.calendarService.editCalendar(this.calendar)
      .subscribe(() => this.goBack());
  }

  goBack(): void {
    this.location.back();
  }

  faChevronLeft = faChevronLeft;

}
