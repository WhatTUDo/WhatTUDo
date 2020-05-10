import {Component, OnInit} from '@angular/core';
import {EventComment} from "../../dtos/event-comment";
import {CalendarEvent} from "../../dtos/calendar-event";
import {Location} from "../../dtos/location";
import {Label} from "../../dtos/label";
import {EventService} from "../../services/event.service";
import {ActivatedRoute} from "@angular/router";

import {faChevronLeft, faTag, faExternalLinkSquareAlt} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {

  constructor(private eventService: EventService,
              private route: ActivatedRoute) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCalendarEvent(id);
  }

  public calendarEvent: CalendarEvent
  public eventTimeString: string = "Event Time"
  public participants: any;


  ngOnInit(): void {

  }

  private loadCalendarEvent(id: number) {
    this.eventService.getEvent(id).subscribe( (event: CalendarEvent) =>{
      this.calendarEvent = event;
      let location = new Location(null, "Fachschaft Informatik", "Treitlstraße 3", "1050", 12.1234, 13.9876);
      this.calendarEvent.comments = this.getComments();
      this.calendarEvent.labels = this.getLabels();
      this.calendarEvent.location = location;
      this.calendarEvent.description = "yololo";
      this.participants = this.getParticipants();
      this.eventTimeString = this.getEventTimeString();
    }, err => {
      alert(err.message);
    })
  }

  public getEventTimeString() {
    let startDateTime: Date = new Date(this.calendarEvent.startDateTime);
    let endDateTime: Date = new Date(this.calendarEvent.endDateTime);
    let string = startDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string +=endDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
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
