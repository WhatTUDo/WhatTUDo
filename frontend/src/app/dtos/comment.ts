export class Comment {
  constructor(
    id: number,
    public authorID: number,
    public content: string,
    public rating: number
  ) {

  }
}
