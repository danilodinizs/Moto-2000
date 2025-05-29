import { UserRole } from "../enums/user-role";

export interface User {
  id?: string;
  username: string;
  password: string;
  role: UserRole;
  createdAt: string;
}
