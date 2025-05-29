import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, map, of, tap, throwError } from 'rxjs';
import { Category } from '../interfaces/category';
import { ApiResponse } from '../interfaces/api-response';
import { Supplier } from '../interfaces/supplier';
import { Product } from '../interfaces/product';
import { User } from '../interfaces/user';
import { UserRole } from '../enums/user-role';

// Interface para o status de autenticação retornado pelo backend
interface AuthStatus {
  isAuthenticated: boolean;
  role: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private static BASE_URL = 'http://localhost:2904/v1/api'; // Mantenha sua URL base

  authStatusChange = new EventEmitter<void>();

  constructor(private http: HttpClient) {}

  private getAuthStatus(): Observable<AuthStatus> {
    const url = `${ApiService.BASE_URL}/auth/status`; // Endpoint a ser criado no backend
    return this.http.get<AuthStatus>(url, { withCredentials: true }).pipe(
      catchError((error) => {
        console.error('Erro ao buscar status de autenticação:', error);
        return of({ isAuthenticated: false, role: null });
      })
    );
  }

  isAuthenticated(): Observable<boolean> {
    return this.getAuthStatus().pipe(map((status) => status.isAuthenticated));
  }

  isAdmin(): Observable<boolean> {
    return this.getAuthStatus().pipe(
      map(
        (status) => status.isAuthenticated && status.role === UserRole.MANAGER
      )
    );
  }

  /***AUTH & USERS API METHODS */

  registerUser(body: User): Observable<ApiResponse<User>> {
    const url = `${ApiService.BASE_URL}/auth/register`;
    return this.http.post<ApiResponse<User>>(url, body);
  }

  loginUser(body: User): Observable<ApiResponse<User>> {
    const url = `${ApiService.BASE_URL}/auth/login`;
    return this.http.post<ApiResponse<User>>(url, { withCredentials: true });
  }

  logout(): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/auth/logout`;
    return this.http
      .post<ApiResponse<null>>(url, {}, { withCredentials: true })
      .pipe(
        tap(() => {
          this.authStatusChange.emit();
        }),
        catchError((error) => {
          console.error('Erro ao fazer logout:', error);
          this.authStatusChange.emit();
          return throwError(() => error);
        })
      );
  }

  getLoggedInUserInfo(): Observable<ApiResponse<User>> {
    const url = `${ApiService.BASE_URL}/users/current`;
    return this.http.get<ApiResponse<User>>(url, { withCredentials: true });
  }

  getAllUsers(): Observable<ApiResponse<User[]>> {
    const url = `${ApiService.BASE_URL}/users/all`;
    return this.http.get<ApiResponse<User[]>>(url, { withCredentials: true });
  }

  updateUser(id: string, body: Partial<User>): Observable<ApiResponse<User>> {
    const url = `${ApiService.BASE_URL}/users/update/${id}`;
    return this.http.patch<ApiResponse<User>>(url, body, {
      withCredentials: true,
    });
  }

  deleteUser(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/users/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  /**CATEGOTY ENDPOINTS */
  createCategory(body: Category): Observable<ApiResponse<Category>> {
    const url = `${ApiService.BASE_URL}/categories/add`;
    return this.http.post<ApiResponse<Category>>(url, body, {
      withCredentials: true,
    });
  }

  updateCategory(
    id: string,
    body: Partial<Category>
  ): Observable<ApiResponse<Category>> {
    const url = `${ApiService.BASE_URL}/categories/update/${id}`;
    return this.http.patch<ApiResponse<Category>>(url, body, {
      withCredentials: true,
    });
  }

  getAllCategories(): Observable<ApiResponse<Category[]>> {
    const url = `${ApiService.BASE_URL}/categories/all`;
    return this.http.get<ApiResponse<Category[]>>(url, {
      withCredentials: true,
    });
  }

  getCategoryById(id: string): Observable<ApiResponse<Category>> {
    const url = `${ApiService.BASE_URL}/categories/${id}`;
    return this.http.get<ApiResponse<Category>>(url, { withCredentials: true });
  }

  deleteCategory(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/categories/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  /** SUPPLIER ENDPOINTS */
  addSupplier(body: Supplier): Observable<ApiResponse<Supplier>> {
    const url = `${ApiService.BASE_URL}/suppliers/save`;
    return this.http.post<ApiResponse<Supplier>>(url, body, {
      withCredentials: true,
    });
  }

  updateSupplier(
    id: string,
    body: Partial<Supplier>
  ): Observable<ApiResponse<Supplier>> {
    const url = `${ApiService.BASE_URL}/suppliers/update/${id}`;
    return this.http.patch<ApiResponse<Supplier>>(url, body, {
      withCredentials: true,
    });
  }

  getAllSuppliers(): Observable<ApiResponse<Supplier[]>> {
    const url = `${ApiService.BASE_URL}/suppliers/all`;
    return this.http.get<ApiResponse<Supplier[]>>(url, {
      withCredentials: true,
    });
  }

  getSupplierById(id: string): Observable<ApiResponse<Supplier>> {
    const url = `${ApiService.BASE_URL}/suppliers/${id}`;
    return this.http.get<ApiResponse<Supplier>>(url, { withCredentials: true });
  }

  deleteSupplier(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/suppliers/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  /**PRODUICTS ENDPOINTS */
  addProduct(
    product: Product,
    imageFile?: File
  ): Observable<ApiResponse<Product>> {
    const url = `${ApiService.BASE_URL}/products/save`;
    const formData = buildProductFormData(product, imageFile);
    return this.http.post<ApiResponse<Product>>(url, formData, {
      withCredentials: true,
    });
  }

  updateProduct(
    product: Product,
    imageFile?: File
  ): Observable<ApiResponse<Product>> {
    const url = `${ApiService.BASE_URL}/products/update`;
    const formData = buildProductFormData(product, imageFile);
    return this.http.patch<ApiResponse<Product>>(url, formData, {
      withCredentials: true,
    });
  }

  getAllProducts(): Observable<ApiResponse<Product[]>> {
    const url = `${ApiService.BASE_URL}/products/all`;
    return this.http.get<ApiResponse<Product[]>>(url, {
      withCredentials: true,
    });
  }

  getProductById(id: string): Observable<ApiResponse<Product>> {
    const url = `${ApiService.BASE_URL}/products/${id}`;
    return this.http.get<ApiResponse<Product>>(url, { withCredentials: true });
  }

  deleteProduct(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/products/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  // --- Exemplo de como fazer outras chamadas autenticadas ---

  /**
   * Exemplo de chamada GET para um recurso protegido.
   * O cookie HttpOnly será enviado automaticamente pelo navegador.
   */
  getSomeProtectedData(): Observable<any> {
    const url = `${ApiService.BASE_URL}/some-resource`;
    return this.http.get<any>(url, { withCredentials: true });
  }

  /**
   * Exemplo de chamada POST para um recurso protegido.
   * O cookie HttpOnly será enviado automaticamente pelo navegador.
   */
  postSomeProtectedData(data: any): Observable<any> {
    const url = `${ApiService.BASE_URL}/some-resource`;
    return this.http.post<any>(url, data, { withCredentials: true });
  }
}

function buildProductFormData(product: Product, imageFile?: File): FormData {
  const formData = new FormData();
  formData.append('name', product.name);
  if (product.sku) formData.append('sku', product.sku);
  formData.append('price', product.price.toString());
  formData.append('stockQuantity', product.stockQuantity.toString());
  formData.append('categoryId', product.categoryId!);
  if (product.id) formData.append('productId', product.id);
  formData.append('supplierId', product.supplierId!);
  if (product.description) formData.append('description', product.description);
  if (product.expiryDate) formData.append('expiryDate', product.expiryDate);
  if (imageFile) formData.append('imageFile', imageFile);
  return formData;
}
