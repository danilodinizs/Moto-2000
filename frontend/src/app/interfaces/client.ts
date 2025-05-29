import { Motorcycle } from "./motorcycle";
import { Transaction } from "./transaction";

export interface Client {
   id: string;
  name: string;
  cpf: string;
  email?: string;
  phoneNumber: string;
  createdAt: string;
  motorcycles?: Motorcycle[];
  transactions?: Transaction[];
}
