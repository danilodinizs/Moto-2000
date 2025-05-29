import { Color } from "../enums/color";
import { MotorcycleType } from "../enums/motorcycle-type";

export interface Motorcycle {
  id: string;
  licensePlate: string;
  model: string;
  year: number;
  color: Color;
  motorcycleType: MotorcycleType;
  clientId: string;
  createdAt: string;
  updatedAt?: string;
}
