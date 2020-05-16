import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {CalendarEvent} from '../dtos/calendar-event';
import {Calendar} from '../dtos/calendar';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private calendarBaseUri: string = this.globals.backendUri + 'calendars'; //supposing it will be smth like this.

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  getAllCalendars(name: string, organisation: string): Observable<Calendar[]>{

    return this.httpClient.get<any[]>(this.calendarBaseUri + '/');
    //supposing it will look smth like this.
  }

  getCalendarById(id: number): Observable<Calendar> {
    console.log("Load Calendar with ID", id);
    return this.httpClient.get<Calendar>(this.calendarBaseUri + '/' + id);
  }


}
