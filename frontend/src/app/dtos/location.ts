export class Location {
  constructor(
    public id: number,
    public name: string,
    public address: string,
    public zip: string,
    public latitude: number,
    public longitude: number,
    public eventIds?: number[],
    public canEdit?: Boolean,
    public canDelete?: Boolean
  ) {

  }
}
