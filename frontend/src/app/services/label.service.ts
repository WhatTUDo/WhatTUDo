import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Label} from "../dtos/label";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";


@Injectable({
  providedIn: 'root'
})
export class LabelService {

  private labelBaseUri: string = this.globals.backendUri + '/labels';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getAll(): Observable<Label[]> {
    return this.httpClient.get<Label[]>(this.labelBaseUri);
  }

  deleteLabel(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.labelBaseUri + `/${id}`);
  }

  createLabel(name: string): Observable<Label> {
    console.log('Post Label to Server', name);
    let reducedElement = {
      'id': null,
      'name': name,
      'events': null
    };

    return this.httpClient.post<Label>(this.labelBaseUri, reducedElement);
  }

}
