import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Location} from "../../dtos/location";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import {faMapMarked} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-event-location',
  templateUrl: './event-location.component.html',
  styleUrls: ['./event-location.component.scss']
})
export class EventLocationComponent implements OnInit, OnChanges {

  @Output() locationSaved = new EventEmitter<Location>();
  @Input() location: Location;
  searchComplete = false
  addressSelected = false;
  selectionComplete = false;
  public searchResults: Array<any>;
  public selectedLocation: Location;
  public previewURL: SafeResourceUrl;
  faMapMarked = faMapMarked;

  constructor(private eventService: EventService,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    console.log("WTF is happening");
    console.log(JSON.stringify(this.location));
    if (this.location) {
      this.createPreviewURL(this.location);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.location) {
      this.createPreviewURL(this.location);
    }
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
    this.createPreviewURL(this.selectedLocation);
    this.addressSelected = true;
  }

  saveButtonClicked() {
    this.searchResults = null;
    this.selectionComplete = true;
    this.locationSaved.emit(this.selectedLocation);
  }

  private createPreviewURL(previewLocation: Location) {
    if (!(previewLocation.latitude && previewLocation.latitude)) {
      this.previewURL = null;
      return;
    }

    let upperBBoxLat: number = previewLocation.latitude - (-0.0002);
    let lowerBBoxLat: number = previewLocation.latitude - 0.0002;
    let upperBBoxLon: number = previewLocation.longitude - (-0.0002);
    let lowerBBoxLon: number = previewLocation.longitude - 0.0002;
    let previewString: string = "https://www.openstreetmap.org/export/embed.html?bbox=" + lowerBBoxLon + "%2C" + lowerBBoxLat + "%2C" + upperBBoxLon + "%2C" + upperBBoxLat + "&amp;layer=mapnik&amp;marker=" + previewLocation.longitude + "%2C" + previewLocation.latitude;

    this.previewURL = this.sanitizer.bypassSecurityTrustResourceUrl(previewString)
  }
}
