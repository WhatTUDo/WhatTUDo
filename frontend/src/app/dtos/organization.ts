export class Organization {
  constructor(
    public id: number,
    public name: string,
    public calendarIds: number[],
    public description?: string,
    public coverImageUrl?: string,
    public canEdit?: Boolean,
    public canDelete?: Boolean
  ) {
  }
}
