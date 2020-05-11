import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Globals {
  readonly backendUri: string = 'http://localhost:8080/';

  readonly openStreetMapsUri: string = 'https://nominatim.openstreetmap.org/search';
}
