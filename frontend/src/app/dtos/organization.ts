export class Organization {
  constructor(
    public id: number,
    public name: string,
    public calendarIds: number[],
    public canEdit: Boolean,
    public canDelete: Boolean
  ) {
  }
}
