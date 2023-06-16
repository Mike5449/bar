import { IEmployee } from "app/entities/employee/employee.model";

export class Registration {
  constructor(public login: string, public password: string, public langKey: string ,public employee:Pick<IEmployee,'id' | 'firstName'> | null) {}
}
