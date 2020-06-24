import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Location} from "../../dtos/location";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import {faMapMarked} from "@fortawesome/free-solid-svg-icons";
import {LocationService} from "../../services/location.service";
import {strict} from "assert";

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
  public searchResults: Array<Location>;
  public selectedLocation: Location;
  public previewURL: SafeResourceUrl;

  public searchString = "";
  faMapMarked = faMapMarked;

  constructor(private eventService: EventService,
              private locationService: LocationService,
              private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    if (this.location) {
      this.createPreviewURL(this.location);
      this.searchString = this.location.name;
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

        this.eventService.searchLocationInAPI(value).subscribe(results => {
          let locationResults = this.mapAPIResultsToLocationObjects(results);

          locationResults.forEach(locationResult => {
            if (!this.searchResults.includes(locationResult)) {
              this.searchResults.push(locationResult);
            }
          });
          this.searchComplete = true;
        });
      })
    })

  }

  addressClicked(result: Location) {
    this.selectedLocation = result;
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

    let upperBoundingBoxLatitude: string = (previewLocation.latitude + 0.0002).toFixed(5);
    let lowerBoundingBoxLatitude: string = (previewLocation.latitude - 0.0002).toFixed(5);
    let upperBoundingBoxLongitude: string = (previewLocation.longitude + 0.0002).toFixed(5);
    let lowerBoundingBoxLongitude: string = (previewLocation.longitude - 0.0002).toFixed(5);
    let previewString: string = "//www.openstreetmap.org/export/embed.html?bbox=" + lowerBoundingBoxLongitude + "%2C" +
      lowerBoundingBoxLatitude + "%2C" + upperBoundingBoxLongitude + "%2C" + upperBoundingBoxLatitude +
      "&amp;layer=mapnik&amp;marker=" + previewLocation.longitude.toFixed(5) + "%2C" +
      previewLocation.latitude.toFixed(5);

    this.previewURL = this.sanitizer.bypassSecurityTrustResourceUrl(previewString)
  }

  private mapAPIResultsToLocationObjects(apiResults: Array<any>): Array<Location> {
    let locations = Array<Location>();
    apiResults.forEach(result => {
      let location = this.parseLocation(result);
      locations.push(location);
    });
    return locations;
  }

  private parseLocation(result): Location {
    let specificName = this.createSpecifiNameForApiResult(result);
    let locationAddress = result.address.road + " " + result.address.postcode + " " + result.address.city;
    let postcode = result.address.postcode ? result.address.postcode : "no zip";
    let newLocation: Location = new Location(null, specificName, locationAddress, postcode, result.lat, result.lon, []);
    return newLocation;
  }

  private createSpecifiNameForApiResult(result): string {
    let name = "";
    if (result.address.office) {
      name = result.address.office;
    } else if (result.address.road) {
      name = result.address.road;
      if (result.address.house_number) {
        name += " " + result.address.house_number;
      }
    }
    return name;
  }
}
