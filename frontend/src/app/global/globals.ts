import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Globals {
  readonly backendUri: string = '//localhost:8080';
  readonly openStreetMapsUri: string = 'https://nominatim.openstreetmap.org/search';

  readonly allowedAvatarMimeTypes: string[] = ['image/jpeg']; // 'image/png', 'image/gif', 'image/svg+xml'
  readonly allowedPromoImageMimeTypes: string[] = ['image/jpeg']; // 'image/png', 'image/svg+xml'
  dateLocale: string = 'en-US';
}
