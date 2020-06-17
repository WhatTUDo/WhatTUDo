export class Calendar {
  constructor(
    public id: number,
    public name: string,
    public eventIds: number[],
    public organizationIds: number[],
    public description?: string,
    public canEdit?: Boolean,
    public canDelete?: Boolean
  ) {
  }
}
