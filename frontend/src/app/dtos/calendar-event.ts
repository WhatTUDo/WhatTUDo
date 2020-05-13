import {Label} from "./label";
import {Location} from "./location";
import {EventComment} from "./event-comment";

export class CalendarEvent {
  constructor(
    id: number,
    public name: string,
    public description: string,
    public startDateTime: Date,
    public endDateTime: Date,
    public location: Location,
    public labels: Array<Label>,
    public comments: Array<EventComment>,
    public calendarId?: number,

  ) {
  }

  isDuringDate(date: Date) {
    date.setHours(0, 0, 0, 0);
    const doesBeginsBeforeDate = this.startDateTime < date;

    date.setHours(24, 0, 0, 0);
    const doesEndsAfterDate = this.endDateTime > date;

    return doesBeginsBeforeDate || doesEndsAfterDate;
  }

  doesBeginOnDate(date: Date) {
    return this.isTheSameDate(this.startDateTime, date);
  }

  doesEndOnDate(date: Date) {
    return this.isTheSameDate(this.endDateTime, date);
  }

  public isTheSameDate(date1: Date, date2: Date) {
    return date1.toDateString() == date2.toDateString();
  }

  public getDisplayTimeString() {
    let string = this.startDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    string += ' - '
    string += this.endDateTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: 'numeric'
    }).replace(":00", "")
    return string
  }
}
