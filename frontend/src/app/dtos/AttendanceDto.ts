import {AttendanceStatusPossibilities} from './AttendanceStatusPossibilities';

export class AttendanceDto {
  constructor(
    public username: string,
    public eventId: number,
    public status: AttendanceStatusPossibilities
  ) {
  }
}
