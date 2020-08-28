export class Label {
  constructor(
    public id: number,
    public name: string,
    public events: Array<Event>,
    public canEdit?: Boolean,
    public canDelete?: Boolean
  ) {

  }
}
