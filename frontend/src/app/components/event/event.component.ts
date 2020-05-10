import {Component, OnInit} from '@angular/core';
import {EventComment} from "../../dtos/event-comment";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {Label} from "../../dtos/label";
import {EventService} from "../../services/event.service";

import {faChevronLeft, faTag, faExternalLinkSquareAlt} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {

  constructor(private eventService: EventService) {
  }

  public calendarEvent: CalendarEvent
  public participants: any;


  ngOnInit(): void {
    let startDate = new Date(2020, 5, 5, 18, 0, 0, 0);
    let endDate = new Date(2020, 5, 5, 22, 30, 0, 0);
    let location = new Location(null, "Fachschaft Informatik", "Treitlstraße 3", "1050", 12.1234, 13.9876);

    this.calendarEvent = new CalendarEvent(null, "Test Event", "Capitalize on low hanging fruit to identify a ballpark value added activity to beta test. Override the digital divide with additional clickthroughs from DevOps. Nanotechnology immersion along the information highway will close the loop on focusing solely on the bottom line.\n" +
      "\n" +
      "Podcasting operational change management inside of workflows to establish a framework. Taking seamless key performance indicators offline to maximise the long tail. Keeping your eye on the ball while performing a deep dive on the start-up mentality to derive convergence on cross-platform integration.\n" +
      "\n" +
      "Collaboratively administrate empowered markets via plug-and-play networks. Dynamically procrastinate B2C users after installed base benefits. Dramatically visualize customer directed convergence without revolutionary ROI.", startDate, endDate, location, null, null);

    this.calendarEvent.comments = this.getComments();
    this.calendarEvent.labels = this.getLabels();
    this.participants = this.getParticipants();


  }

  private loadCalendarEvent(id: number) {
    this.eventService.getEvent(id).subscribe( (event: CalendarEvent) =>{
      this.calendarEvent = event;
    }, err => {
      //display Error
    })
  }

  private deleteEvent(){
    this.eventService.deleteEvent(this.calendarEvent).subscribe(() => {
      alert("Event deleted");
    }, error => {
      // display Error.
    })
  }


  public participate(status: number) {
    switch (status) {
      case 0:
        alert("You declined!");
        break;
      case 1:
        alert("You are attending!");
        break;
      case 2:
        alert("You are interested!");
        break;
      default:
        alert("No idea what you want!");
        break;
    }
  }

  public addComment() {
    let textArea: any = document.getElementById('comment-area');
    if (textArea) {
      let comment = textArea.value;
      if (comment || comment.length > 0) {
        alert("Comments aren't live yet, but here's what you wrote: " + comment);
      } else {
        alert('Could not read comment!');
      }
    }
  }

  private getParticipants() {
    let participants: any = {
      "attending": [],
      "interested": [],
      "declined": []
    }
    participants.attending.push("George Ford", "Claudia Straka", "Steve McIntosh");
    participants.interested.push("Martin Eyre", "Carla Angular", "Marcus Cable");
    participants.declined.push("Ken Scotland");

    return participants;
  }

  private getComments() {
    let comment1 = new EventComment(null, null, "Leverage agile frameworks to provide a robust synopsis for high level overviews. Iterative approaches to corporate strategy foster collaborative thinking to further the overall value proposition. Organically grow the holistic world view of disruptive innovation via workplace diversity and empowerment.\n" +
      "\n", 0.85);
    let comment2 = new EventComment(null, null, "Bring to the table win-win survival strategies to ensure proactive domination. At the end of the day, going forward, a new normal that has evolved from generation X is on the runway heading towards a streamlined cloud solution. User generated content in real-time will have multiple touchpoints for offshoring.\n" +
      "\n", 0.66);
    let comment3 = new EventComment(null, null, "Capitalize on low hanging fruit to identify a ballpark value added activity to beta test. Override the digital divide with additional clickthroughs from DevOps. Nanotechnology immersion along the information highway will close the loop on focusing solely on the bottom line.\n" +
      "\n", 0.91);
    let array = new Array<EventComment>();
    array.push(comment1, comment2, comment3);

    return array;
  }

  private getLabels() {
    let label1 = new Label(null, "Party");
    let label2 = new Label(null, "Festl");
    let array = new Array<Label>();
    array.push(label1, label2);

    return array;
  }

  faChevronLeft = faChevronLeft;
  faTag = faTag
  faExternalLinkSquareAlt = faExternalLinkSquareAlt;
}
