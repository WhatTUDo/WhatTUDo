import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Location} from "../dtos/location";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private locationBaseUri: string = this.globals.backendUri + "/locations";

  constructor(private httpClient: HttpClient,
              private globals: Globals) {
  }

  saveLocation(location: Location): Observable<Location> {
    console.log("saving new Location: ", location.name);
    let uri = this.locationBaseUri
    return this.httpClient.post<Location>(uri, location);
  }

  getLocation(id: number): Observable<Location> {
    return this.httpClient.get<Location>(this.locationBaseUri + "/" + id);
  }

  searchName(searchterm: string): Observable<Array<Location>> {
    // let searchParams = new HttpParams();
    // searchParams.append("search", searchterm);
    let url = "?name=" + searchterm;

    return this.httpClient.get<Array<Location>>(this.locationBaseUri + "/searchName" + url);
  }

  searchAddress(searchterm: string): Observable<Array<Location>> {
    // let searchParams = new HttpParams();
    // searchParams.append("search", searchterm);
    let url = "?address=" + searchterm;
    return this.httpClient.get<Array<Location>>(this.locationBaseUri + "/searchAddress" + url);
  }
}
