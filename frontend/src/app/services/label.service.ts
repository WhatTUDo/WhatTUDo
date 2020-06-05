import { Injectable } from '@angular/core';
import {AuthRequest} from "../dtos/auth-request";
import {Observable} from "rxjs";
import {CalendarEvent} from "../dtos/calendar-event";
import {Label} from "../dtos/label";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {EventComment} from "../dtos/event-comment";

@Injectable({
  providedIn: 'root'
})
export class LabelService {

private labelBaseUri: string = this.globals.backendUri + 'labels';

constructor(private httpClient: HttpClient, private globals: Globals) {
}

  getAll(): Observable<Label[]> {

    console.log('Get all labels');
    return this.httpClient.get<Label[]>(this.labelBaseUri);
  }

}
