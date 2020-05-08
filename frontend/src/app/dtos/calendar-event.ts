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

  // Utility functions are probably for somewhere else.
  // I'm coding here until such place is built.

  isDuringDate(date: Date) {
    date.setHours(0, 0, 0, 0);
    const doesBeginsBeforeDate = this.startDate < date;

    date.setHours(24, 0, 0, 0);
    const doesEndsAfterDate = this.endDate > date;

    return doesBeginsBeforeDate || doesEndsAfterDate;
  }

  doesBeginOnDate(date: Date) {
    return this.isTheSameDate(this.startDate, date);
  }

  doesEndOnDate(date: Date) {
    return this.isTheSameDate(this.endDate, date);
  }

  isTheSameDate(date1: Date, date2: Date) {
    return date1.toDateString() == date2.toDateString();
  }
}
