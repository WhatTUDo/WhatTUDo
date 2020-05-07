import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {

  reactiveEventForm = new FormGroup( {
    id: new FormControl(''),
    name: new FormControl(''),
    startTime: new FormControl(''),
    endTime: new FormControl(''),
    location: new FormControl(''),
    labels: new FormControl(''),
  });

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit() {
    let formValue = this.reactiveEventForm.value;

    console.log(formValue);
  }

}
