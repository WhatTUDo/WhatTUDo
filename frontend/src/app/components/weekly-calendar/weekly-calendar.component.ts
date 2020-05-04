import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-weekly-calendar',
  templateUrl: './weekly-calendar.component.html',
  styleUrls: ['./weekly-calendar.component.scss']
})
export class WeeklyCalendarComponent implements OnInit {

  currentDate: number;
  currentMonth: String;

  constructor() {
  }

  ngOnInit(): void {
    this.updateDatetime();
    setInterval(_=>{this.updateDatetime();}, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentMonth = date.toLocaleString('en-US', { month: 'long' });
    this.currentDate = date.getDate();
  }


}
