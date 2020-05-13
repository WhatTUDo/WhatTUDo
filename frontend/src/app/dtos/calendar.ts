export class Calendar{
  constructor(
    id : number,
    public name : string,
    public eventIds ?: number[],
    public organisationIds ?: number[]
  ) {
  }
}
