import { Component, OnInit, Input } from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {CalendarService} from '../../services/calendar.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-calendar-edit',
  templateUrl: './calendar-edit.component.html',
  styleUrls: ['./calendar-edit.component.css']
})

export class CalendarEditComponent implements OnInit {
@Input() calendar: Calendar;

calendar1 : Calendar;


  constructor(
    private route: ActivatedRoute,
    private calendarService: CalendarService,
  ) { }



  ngOnInit(): void {

    this.getCalendar();
  }

  getCalendar(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.calendarService.getCalendarById(id)
      .subscribe(calendar => this.calendar = calendar);
  }

  update(): void {

  this.calendarService.editCalendar(this.calendar);
}

}
