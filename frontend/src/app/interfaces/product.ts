export interface Product {
  id?: string;
  name: string;
  sku?: string;
  price: number;
  stockQuantity: number;
  description?: string;
  expiryDate?: string;
  imageUrl?: string;
  productId?: string;
  categoryId: string;
  supplierId: string;
  createdAt?: string;
  updatedAt?: string;
}
