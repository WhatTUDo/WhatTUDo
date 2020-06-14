export class ChangeUserPasswordDto {
  constructor(public username: string,
              public email: string,
              public currentPassword: string,
              public newPassword: string) {
  }
}
