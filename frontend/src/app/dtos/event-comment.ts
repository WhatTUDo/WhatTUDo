export class EventComment {
  constructor(
    id: number,
    public authorID: number,
    public content: string,
    public rating: number
  ) {

  }
}
