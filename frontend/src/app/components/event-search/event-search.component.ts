import {Component, OnInit} from '@angular/core';
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";
import {FormControl, FormGroup} from "@angular/forms";
import {EventService} from "../../services/event.service";
import {CalendarEvent} from "../../dtos/calendar-event";

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {

  faChevronLeft = faChevronLeft;

  searchForm = new FormGroup({
    name: new FormControl('')
  });
  searchActive: boolean = false;
  eventSearchResult: CalendarEvent[] = [];

  constructor(private eventService: EventService) {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    let formValue = this.searchForm.value;
    if (!formValue.name) {
      this.eventSearchResult = [];
      this.searchActive = false;
      return
    }
    let validationIsPassed = this.validateFormInput(formValue);
    if (validationIsPassed) {
      // submit to service
      console.log("search");
      this.eventService.searchEvent(formValue.name).subscribe((searchResult)=>{
        this.eventSearchResult = searchResult;
        this.searchActive = true;
      })
    }
  }


  /**
   *
   * @param formValue
   * returns true/false depending on whether validation succeeds.
   */
  validateFormInput(formValue: any) {
    let errors: Array<Error> = new Array<Error>();
    if ((formValue.name == "")) {
      errors.push(new Error("Nothing was given to search."));
    }

    if (errors.length > 0) {
      console.warn(errors);
      let errorMessage = "";
      for (let error of errors) {
        errorMessage += error.message + " ";
      }
      alert(errorMessage);
      this.eventSearchResult = [];
      return false;
    }
    return true;
  }

  clearSearch() {
    this.searchForm = new FormGroup({
      name: new FormControl('')
    });
    this.eventSearchResult = [];
    this.searchActive = false;
  }

}
