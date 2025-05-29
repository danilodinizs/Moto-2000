export interface ServiceOrder {
  id: string;
  name: string;
  description: string;
  duration: string; 
  laborCost: number;
  createdAt: string;
  productsIds?: string[];
}
