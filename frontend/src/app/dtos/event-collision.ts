import {CalendarEvent} from "./calendar-event";

export class EventCollision {
  constructor(
    public event: CalendarEvent,
    public collisionScore: number,
    public message: string
  ) {
  }
}
