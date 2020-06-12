import {Label} from "./label";
import {Location} from "./location";
import {EventComment} from "./event-comment";

export class CalendarEvent {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public startDateTime: Date,
    public endDateTime: Date,
    public location: Location,
    public labels: Array<Label>,
    public comments: Array<EventComment>,
    public calendarId: number,
    public canEdit?: Boolean,
    public canDelete?: Boolean
  ) {
  }
}
