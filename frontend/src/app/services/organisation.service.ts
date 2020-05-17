import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Organisation} from '../dtos/organisation';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})

export class OrganisationService {

  private organisationBaseUri: string = this.globals.backendUri + 'organisations';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Posts organisation to Server --> New Organisation
   * @param organisation
   */
  postOrganisation(organisation: Organisation): Observable<any> {
    console.log('Post Organisation to Server', Organisation);
    let reducedElement = {
      'name': organisation.name,
      'calendars': organisation.calendars
    };

    return this.httpClient.post(this.organisationBaseUri, reducedElement);
  }

  getAll(): Observable<Organisation[]> {
    console.log('Get all orgas');
    return this.httpClient.get<Organisation[]>(this.organisationBaseUri);
  }

  getById(id: number): Observable<Organisation> {
    console.log('Get orga with ID', id);
    return this.httpClient.get<Organisation>(this.organisationBaseUri + '/' + id);
  }

  /**
   * Puts organisation to Server --> Update Organisation
   * @param organisation updated version of organisation to be saved
   */
  putOrganisation(organisation: Organisation): Observable<any> {
    console.log('Put Organisation to Server', Organisation);
    return this.httpClient.put(this.organisationBaseUri, organisation);
  }

}
