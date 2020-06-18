import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";
import {Calendar} from "../dtos/calendar";
import {SubscriptionDto} from "../dtos/subscriptionDto";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  private subscriptionUri = this.globals.backendUri + "subscription";

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
  ) {
  }

  create(subscription: SubscriptionDto): Observable<SubscriptionDto> {
    return this.httpClient.post<SubscriptionDto>(this.subscriptionUri + "/", subscription);
  }

  delete(subscription: SubscriptionDto): Observable<void> {
    // return this.httpClient.delete<SubscriptionDto>(this.subscriptionUri + "/"); //FIXME
    return new Observable<void>();
  }

  getSubscribedCalendars(userId: number): Observable<Array<Calendar>> {
    return this.httpClient.get<Array<Calendar>>(this.subscriptionUri + "/calendars/" + userId);
  }

  getCalendarSubscribers(calendarId: number): Observable<Array<Calendar>> {
    return this.httpClient.get<Array<Calendar>>(this.subscriptionUri + "/users/" + calendarId);
  }
}
