import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Location} from "../../dtos/location";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-event-location',
  templateUrl: './event-location.component.html',
  styleUrls: ['./event-location.component.scss']
})
export class EventLocationComponent implements OnInit {

  @Output() locationSaved = new EventEmitter<Location>();

  constructor(private eventService: EventService,
              private sanitizer: DomSanitizer) {
  }

  private searchComplete = false
  private addressSelected = false;
  private selectionComplete = false;
  public searchResults: Array<any>;
  public selectedLocation: Location;
  public previewURL: SafeResourceUrl;


  ngOnInit(): void {

  }

  performSearch() {
    this.selectionComplete = false;
    let element: any = document.getElementById('addressSearch');
    let value = element.value;
    this.eventService.searchLocationInAPI(value).subscribe(results => {
      this.searchResults = results;
      this.searchComplete = true;
      console.log(results);
    }, err => {
      console.warn(err);
    })
  }

  addressClicked(result: any) {
    let specificName = result.address.address29 ? result.address29 : result.address.road
    let locationAddress = result.address.road + " " + result.address.postcode + " " + result.address.city;
    let location: Location = new Location(null, specificName, locationAddress, result.address.postcode, result.lat, result.lon);
    this.selectedLocation = location;
    console.log(location);
    this.createPreviewURL();
    this.addressSelected = true;
  }

  saveButtonClicked() {
    this.searchResults = null;
    this.selectionComplete = true;
    this.locationSaved.emit(this.selectedLocation);
  }

  private createPreviewURL() {
    let upperBBoxLat: number = this.selectedLocation.latitude - (-0.0002);
    let lowerBBoxLat: number = this.selectedLocation.latitude - 0.0002;
    let upperBBoxLon: number = this.selectedLocation.longitude - (-0.0002);
    let lowerBBoxLon: number = this.selectedLocation.longitude - 0.0002;
    let previewString: string = "https://www.openstreetmap.org/export/embed.html?bbox=" + lowerBBoxLon + "%2C" + lowerBBoxLat + "%2C" + upperBBoxLon + "%2C" + upperBBoxLat + "&amp;layer=mapnik&amp;marker=" + this.selectedLocation.longitude + "%2C" + this.selectedLocation.latitude;

    console.log(previewString);
    this.previewURL = this.sanitizer.bypassSecurityTrustResourceUrl(previewString)
  }

}
