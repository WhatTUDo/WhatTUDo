export class AttendanceDto {
  constructor(
    public userId: number,
    public eventId : number,
    public attendanceStatus: string,
  ) {}
}
