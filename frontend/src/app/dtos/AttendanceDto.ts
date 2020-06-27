import {AttendanceStatusPossibilities} from './AttendanceStatusPossibilities';

export class AttendanceDto {
  constructor(
    public id: number,
    public username: string,
    public eventId: number,
    public status: AttendanceStatusPossibilities
  ) {
  }
}
