import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Location} from "../../dtos/location";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import {faMapMarked} from "@fortawesome/free-solid-svg-icons";
import {LocationService} from "../../services/location.service";

@Component({
  selector: 'app-event-location',
  templateUrl: './event-location.component.html',
  styleUrls: ['./event-location.component.scss']
})
export class EventLocationComponent implements OnInit, OnChanges {

  @Output() locationSaved = new EventEmitter<Location>();
  @Input() location: Location;
  @Input() editable: boolean = true;
  searchComplete = false
  addressSelected = false;
  selectionComplete = false;
  public searchResults: Array<any>;
  public selectedLocation: Location;
  public previewURL: SafeResourceUrl;
  faMapMarked = faMapMarked;

  constructor(private eventService: EventService,
              private locationService: LocationService,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    if (this.location) {
      this.createPreviewURL(this.location);
    } else {
      this.previewURL = null;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.location) {
      this.createPreviewURL(this.location);
    } else {
      this.previewURL = null;
    }
  }

  performSearch() {
    this.selectionComplete = false;
    let element: any = document.getElementById('addressSearch');
    let value = element.value;
    this.locationService.searchName(value).subscribe(resultsForName => {
      this.searchResults = resultsForName;

      this.locationService.searchAddress(value).subscribe(resultsForAddress => {
        resultsForAddress.forEach(result => {
          if (!this.searchResults.includes(result)) {
            this.searchResults.push(result);
          }
        })
      })
    })
    this.eventService.searchLocationInAPI(value).subscribe(results => {
      this.searchResults = results;
      this.searchComplete = true;
    });
  }

  addressClicked(result: any) {
    let specificName = result.address.address29 ? result.address29 : result.address.road
    let locationAddress = result.address.road + " " + result.address.postcode + " " + result.address.city;
    let location: Location = new Location(null, specificName, locationAddress, result.address.postcode, result.lat, result.lon, []);
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
    previewLocation.latitude = +previewLocation.latitude;
    previewLocation.longitude = +previewLocation.longitude;
    if (!(previewLocation.latitude && previewLocation.longitude)) {
      this.previewURL = null;
      return;
    }

    let upperBBoxLat: string = (previewLocation.latitude + 0.0002).toFixed(5);
    let lowerBBoxLat: string = (previewLocation.latitude - 0.0002).toFixed(5);
    let upperBBoxLon: string = (previewLocation.longitude + 0.0002).toFixed(5);
    let lowerBBoxLon: string = (previewLocation.longitude - 0.0002).toFixed(5);
    let previewString: string = "//www.openstreetmap.org/export/embed.html?bbox=" + lowerBBoxLon + "%2C" + lowerBBoxLat + "%2C" + upperBBoxLon + "%2C" + upperBBoxLat + "&amp;layer=mapnik&amp;marker=" + previewLocation.longitude.toFixed(5) + "%2C" + previewLocation.latitude.toFixed(5);

    this.previewURL = this.sanitizer.bypassSecurityTrustResourceUrl(previewString)
  }
}
