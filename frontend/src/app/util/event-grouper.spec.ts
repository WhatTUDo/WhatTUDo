import {TestBed} from '@angular/core/testing';
import {EventGrouper} from './event-grouper';
import {CalendarEvent} from '../dtos/calendar-event';

describe('EventGrouper', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const eventGrouper: EventGrouper = TestBed.get(EventGrouper);
    expect(eventGrouper).toBeTruthy();
  });

  it('should sort', () => {
    const eventGrouper: EventGrouper = TestBed.get(EventGrouper);

    const e1 = makeEvent(1, 'Event1', 1, 0, 1, 12);
    const e2 = makeEvent(2, 'Event2', 1, 12, 2, 12);
    const e3 = makeEvent(3, 'Event3', 2, 12, 4, 12);
    const e4 = makeEvent(4, 'Event4', 4, 11, 4, 14);

    const grouped = eventGrouper.byDay([e1, e2, e3, e4]);

    expect(grouped[new Date(2020, 0, 1).getTime()]).toEqual([e1, e2]);
    expect(grouped[new Date(2020, 0, 2).getTime()]).toEqual([e2, e3]);
    expect(grouped[new Date(2020, 0, 3).getTime()]).toEqual([e3]);
    expect(grouped[new Date(2020, 0, 4).getTime()]).toEqual([e3, e4]);
  });
});

const makeEvent = (id: number, name: string, day1: number, hour1: number, day2: number, hour2: number) => new CalendarEvent(
  id, name, null,
  new Date(2020, 0, day1, hour1, 0),
  new Date(2020, 0, day2, hour2, 0),
  null, null, null);
