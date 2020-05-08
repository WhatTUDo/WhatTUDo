import { Component, OnInit } from '@angular/core';
import {EventService} from "../../services/event.service";
import {Location} from "../../dtos/location";

@Component({
  selector: 'app-event-location',
  templateUrl: './event-location.component.html',
  styleUrls: ['./event-location.component.css']
})
export class EventLocationComponent implements OnInit {

  constructor(private eventService: EventService) { }

  private searchComplete = false
  private addressSelected = false;
  public searchResults: Array<any>

  ngOnInit(): void {
  }

  performSearch() {
    let element: any = document.getElementById('addressSearch');
    let value = element.value;
    this.eventService.searchLocationInAPI(value).subscribe( results => {
      this.searchResults = results;
      this.searchComplete = true;
      console.log(results);
    }, err => {
      console.warn(err);
    })
  }

  addressClicked(result: any) {
    let specificName = result.address.address29 ? result.address29 : result.address.road
    let locationAddress = result.address.road + " " +  result.address.postcode + " " +  result.address.city;
     let location: Location = new Location(null, specificName, locationAddress, result.address.postcode, result.lat, result.lon);
     console.log(location);
  }

}
