import {Label} from "./label";
import {Location} from "./location";
import {Comment} from "./comment";

export class CalendarEvent {
  constructor(
    id: number,
    public name: string,
    public startDate: Date,
    public endDate: Date,
    public location: Location,
    public labels: [Label],
    public comments: [Comment]
  ) {
  }
}
