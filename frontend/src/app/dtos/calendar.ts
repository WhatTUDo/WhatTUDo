export class Calendar {
  constructor(
    public id: number,
    public name: string,
    public eventIds: number[],
    public organizationIds: number[],
    public description?: string,
    public coverImageUrl?: string,
    public canCreateEvents?: boolean,
    public canEdit?: boolean,
    public canDelete?: boolean
  ) {
  }
}
