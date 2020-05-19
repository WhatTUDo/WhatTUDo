
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Organization} from '../dtos/organization';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})

export class OrganizationService {

  private organizationBaseUri: string = this.globals.backendUri + 'organizations';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Posts organization to Server --> New Organization
   * @param organization
   */
  postOrganization(organization: Organization): Observable<any> {

    console.log('Post Organization to Server', Organization);
    let reducedElement = {
      'name': organization.name,
      'calendars': organization.calendars
    };

    return this.httpClient.post(this.organizationBaseUri, reducedElement);
  }

  getAll(): Observable<Organization[]> {

    console.log('Get all orgas');
    return this.httpClient.get<Organization[]>(this.organizationBaseUri);
  }

  getById(id: number): Observable<Organization> {

    console.log('Get orga with ID', id);
    return this.httpClient.get<Organization>(this.organizationBaseUri + '/' + id);
  }

  /**
   * Puts organization to Server --> Update Organization
   * @param organization updated version of organization to be saved
   */
  putOrganization(organization: Organization): Observable<any> {
    console.log('Put Organization to Server', Organization);
    return this.httpClient.put(this.organizationBaseUri, organization);
  }

}
