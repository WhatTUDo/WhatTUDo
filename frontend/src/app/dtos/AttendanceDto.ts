import {AttendanceStatusPossibilities} from './AttendanceStatusPossibilities';

export class AttendanceDto {
  constructor(
    public userId: number,
    public eventId: number,
    public status: AttendanceStatusPossibilities
  ) {
  }
}
