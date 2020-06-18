import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Organization} from '../dtos/organization';
import {HttpClient, HttpParams, HttpRequest} from '@angular/common/http';
import {Globals} from '../global/globals';
import {User} from "../dtos/user";

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

    console.log('Post Organization to Server', organization);
    let reducedElement = {
      'id': null,
      'name': organization.name,
      'calendarIds': organization.calendarIds
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
    console.log('Put Organization to Server', organization);
    return this.httpClient.put(this.organizationBaseUri, organization);
  }

  /**
   * Deletes Organization and removes all associated calendars.
   * @param id
   */
  deleteOrganization(id: number): Observable<number> {
    console.log("Delete Organization with ID ", id);
    return this.httpClient.delete<number>(this.organizationBaseUri + "/" + id);
  }

  /**
   * @param organizationId ID of organization where calendar should be added.
   * @param calendarId ID of calendar to be added.
   */
  addCalendarToOrga(organizationId: number, calendarId: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', String(calendarId));
    console.log(params);
    return this.httpClient.put(this.organizationBaseUri + `/${organizationId}/calendars`, {}, {
      params: params
    });
  }

  /**
   * @param organizationId ID of organization where calendar should be added.
   * @param calendarId ID of calendar to be added.
   */
  removeCalendarToOrga(organizationId: number, calendarId: number): Observable<any> {
    let params = new HttpParams();
    params = params.set('id', String(calendarId));
    return this.httpClient.delete(this.organizationBaseUri + `/${organizationId}/calendars`, {
      params: params
    })
  }

  /**
   * Get all members of an organization.
   * @param organizationId
   */
  getMembers(organizationId: number): Observable<User[]> {
    console.log(`Get members of organization ${organizationId}`);
    return this.httpClient.get<User[]>(`${this.organizationBaseUri}/members/${organizationId}`);
  }

  getOrganizationAvatarLink(organizationId: number, size: number) {
    // Return base64 of a 1x1px transparent gif if no organizationId is given.
    if (!organizationId) return "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
    return "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="
    //TODO: Implement this after backend is done.
  }

  uploadOrganizationAvatar(organizationId: number, file: File) {
    const formData: FormData = new FormData();

    formData.append('imagefile', file);

    const req = new HttpRequest('POST', `${this.organizationBaseUri}/${organizationId}/cover`,
      formData, {
        responseType: 'json'
      });

    return this.httpClient.request(req);
  }

  getOrganizationAvatar(organizationId: number) {
    return this.httpClient.get(`${this.organizationBaseUri}/${organizationId}/cover`)
  }
}
