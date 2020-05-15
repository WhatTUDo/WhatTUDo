import { Component, OnInit } from '@angular/core';
import {Calendar} from '../../dtos/calendar';
import {Router,ActivatedRoute, ParamMap} from '@angular/router';
import {Observable} from 'rxjs';
import {CalendarService} from '../../services/calendar.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  calendar : Calendar;

  constructor(private route: ActivatedRoute, private calendarService : CalendarService) {

  }

  ngOnInit(): void {
    let id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.calendarService.getCalendarById(id).subscribe((calendar:Calendar) =>{
      this.calendar = calendar;
    });
  }




}
