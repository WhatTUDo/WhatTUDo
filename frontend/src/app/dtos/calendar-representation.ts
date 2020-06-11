import {Organization} from './organization';

export class CalendarRepresentation {
  constructor(
    public id: number,
    public name ?: string,
    public organization ?: Organization[]
  ) {
  }
}
