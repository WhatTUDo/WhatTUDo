import {Label} from "./label";
import {Location} from "./location";
import {EventComment} from "./event-comment";

export class CalendarEvent {
  constructor(
    id: number,
    public name: string,
    public description: string,
    public startDate: Date,
    public endDate: Date,
    public location: Location,
    public labels: Array<Label>,
    public comments: Array<EventComment>
  ) {
  }
}
