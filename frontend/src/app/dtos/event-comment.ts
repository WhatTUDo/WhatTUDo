export class EventComment {
  constructor(
    public id: number,
    public text: string,
    public username: string,
    public eventId: number,
    public updateDateTime: Date

   // public rating: number
  ) {

  }
}
