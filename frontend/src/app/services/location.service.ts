import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Location} from "../dtos/location"

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  private locationBaseUri: string = this.globals.backendUri + '/locations';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  getById(id: number){
    return this.httpClient.get<Location>(this.locationBaseUri + `/${id}`);
  }

  create(location: Location) {
    let reducedElement = {
      id: null,
      name: location.name,
      address: location.address,
      zip: location.zip,
      latitude: location.latitude,
      longitude: location.longitude,
    }
    return this.httpClient.post<Location>(this.locationBaseUri, reducedElement);
  }
}
