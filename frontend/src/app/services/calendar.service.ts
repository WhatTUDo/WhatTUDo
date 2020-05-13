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
  private messageBaseUri: string = this.globals.backendUri + '/calendar'; //supposing it will be smth like this.

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  searchCalendar(name: string, organisation: string): Observable<Calendar[]>{
    let params = new HttpParams().set("name", name)
      .set("organisation", organisation);
    return this.httpClient.get<any[]>(this.messageBaseUri + '/search', {params: params});
    //supposing it will look smth like this.
  }


}
