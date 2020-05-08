import {CalendarEvent} from '../dtos/calendar-event';
import {Injectable} from '@angular/core';


const getDates = (it: CalendarEvent) => {
  const dates: number[] = [];

  let currentDate = it.startDate.setHours(0, 0, 0, 0);
  while (currentDate < it.endDate.getTime()) {
    dates.push(currentDate);
    currentDate += 24 * 60 * 60 * 1000;
  }
  return dates;
};

const concat = (x, y) =>
  x.concat(y);

const flatMap = (f, xs) =>
  xs.map(f).reduce(concat, []);

@Injectable({
  providedIn: 'root'
})
export class EventGrouper {
  /**
   * Takes an array of events and creates a map of dates and the events happening on them
   * @param events to use
   */
  byDay: (events: CalendarEvent[]) => Map<number, CalendarEvent[]> = events => {
    const uniqueDatesSorted: number[] = flatMap(it => getDates(it), events) // Get all valid dates
      .filter((v, i, a) => a.indexOf(v) === i) // Gets only a single instance of each
      .sort((a, b) => a - b); // Sorts them

    const dateMap = new Map<number, CalendarEvent[]>();
    uniqueDatesSorted.forEach(date => {
      events.forEach(event => {
        if (event.startDate.getTime() <= new Date(date).setHours(23, 59, 59, 999) && date <= event.endDate.getTime()) {
          if (dateMap[date]) {
            dateMap[date] = [...dateMap[date], event];
          } else {
            dateMap[date] = [event];
          }
        }
      });
    });
    return dateMap;
  }
}
