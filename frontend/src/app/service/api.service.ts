import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, of, tap, throwError } from 'rxjs';
import { Category } from '../interfaces/category';
import { ApiResponse } from '../interfaces/api-response';
import { Supplier } from '../interfaces/supplier';
import { Product } from '../interfaces/product';
import { User } from '../interfaces/user';
import { UserRole } from '../enums/user-role';
import { Motorcycle } from '../interfaces/motorcycle';
import { ServiceOrder } from '../interfaces/service-order';
import { LoginRequest } from '../interfaces/login-request';
import { Client } from '../interfaces/client';
import { id } from '@swimlane/ngx-charts';
import { Transaction } from '../interfaces/transaction';
import { TransactionRequest } from '../interfaces/transaction-request';
import { TransactionStatus } from '../enums/transaction-status';
import { Router } from '@angular/router';

interface AuthStatus {
  isAuthenticated: boolean;
  role: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private static BASE_URL = 'http://localhost:2904/v1/api';

  private authStatusSubject = new BehaviorSubject<AuthStatus>({ isAuthenticated: false, role: null });

  public isAuthenticated$: Observable<boolean> = this.authStatusSubject.asObservable().pipe(map(status => status.isAuthenticated));
  public isAdmin$: Observable<boolean> = this.authStatusSubject.asObservable().pipe(map(status => status.isAuthenticated && status.role === UserRole.MANAGER));

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus().subscribe();
  }

  checkAuthStatus(): Observable<AuthStatus> {
    const url = `${ApiService.BASE_URL}/auth/status`;
    return this.http.get<AuthStatus>(url, { withCredentials: true }).pipe(
      catchError(() => of({ isAuthenticated: false, role: null })),
      tap(status => this.authStatusSubject.next(status))
    );
  }

  loginUser(body: LoginRequest): Observable<any> {
    const url = `${ApiService.BASE_URL}/auth/login`;
    return this.http.post(url, body, { withCredentials: true }).pipe(
      tap(() => {
        this.checkAuthStatus().subscribe();
      })
    );
  }

  logout(): void {
    const url = `${ApiService.BASE_URL}/auth/logout`;
    this.http.post(url, {}, { withCredentials: true }).pipe(
      catchError(error => {
        console.error('Erro no logout do backend, mas forçando logout no frontend.', error);
        return of(null);
      })
    ).subscribe(() => {
      this.authStatusSubject.next({ isAuthenticated: false, role: null });
      this.router.navigate(['/login']);
    });
  }

  registerUser(body: User): Observable<ApiResponse<User>> {
    const url = `${ApiService.BASE_URL}/auth/register`;
    return this.http.post<ApiResponse<User>>(url, body);
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
  createSupplier(body: Supplier): Observable<ApiResponse<Supplier>> {
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
  createProduct(
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

  /** MOTORCYCLE ENDPOINTS */
  createMotorcycle(body: Motorcycle): Observable<ApiResponse<Motorcycle>> {
    const url = `${ApiService.BASE_URL}/motorcycles/save`;
    return this.http.post<ApiResponse<Motorcycle>>(url, body, {
      withCredentials: true,
    });
  }

  updateMotorcycle(
    id: string,
    body: Partial<Motorcycle>
  ): Observable<ApiResponse<Motorcycle>> {
    const url = `${ApiService.BASE_URL}/motorcycles/update/${id}`;
    return this.http.patch<ApiResponse<Motorcycle>>(url, body, {
      withCredentials: true,
    });
  }

  getAllMotorcycles(): Observable<ApiResponse<Motorcycle[]>> {
    const url = `${ApiService.BASE_URL}/motorcycles/all`;
    return this.http.get<ApiResponse<Motorcycle[]>>(url, {
      withCredentials: true,
    });
  }

  getMotorcycleById(id: string): Observable<ApiResponse<Motorcycle>> {
    const url = `${ApiService.BASE_URL}/motorcycles/${id}`;
    return this.http.get<ApiResponse<Motorcycle>>(url, {
      withCredentials: true,
    });
  }

  deleteMotorcycle(id: string): Observable<ApiResponse<Motorcycle>> {
    const url = `${ApiService.BASE_URL}/motorcycles/delete/${id}`;
    return this.http.delete<ApiResponse<Motorcycle>>(url, {
      withCredentials: true,
    });
  }

  /**SERVICE ORDER ENDPOINTS */
  createServiceOrder(
    body: ServiceOrder
  ): Observable<ApiResponse<ServiceOrder>> {
    const url = `${ApiService.BASE_URL}/service-orders/save`;
    return this.http.post<ApiResponse<ServiceOrder>>(url, body, {
      withCredentials: true,
    });
  }

  updateServiceOrder(
    id: string,
    body: Partial<ServiceOrder>
  ): Observable<ApiResponse<ServiceOrder>> {
    const url = `${ApiService.BASE_URL}/service-orders/update/${id}`;
    return this.http.patch<ApiResponse<ServiceOrder>>(url, body, {
      withCredentials: true,
    });
  }

  getAllServiceOrders(): Observable<ApiResponse<ServiceOrder[]>> {
    const url = `${ApiService.BASE_URL}/service-orders/all`;
    return this.http.get<ApiResponse<ServiceOrder[]>>(url, {
      withCredentials: true,
    });
  }

  getServiceOrderById(id: string): Observable<ApiResponse<ServiceOrder>> {
    const url = `${ApiService.BASE_URL}/service-orders/${id}`;
    return this.http.get<ApiResponse<ServiceOrder>>(url, {
      withCredentials: true,
    });
  }

  deleteServiceOrder(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/service-orders/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  /**CLIENT ENDPOINTS */
  createClient(body: Client): Observable<ApiResponse<Client>> {
    const url = `${ApiService.BASE_URL}/clients/add`;
    return this.http.post<ApiResponse<Client>>(url, body, {
      withCredentials: true,
    });
  }

  updateClient(
    id: string,
    body: Partial<Client>
  ): Observable<ApiResponse<Client>> {
    const url = `${ApiService.BASE_URL}/clients/update/${id}`;
    return this.http.patch<ApiResponse<Client>>(url, body, {
      withCredentials: true,
    });
  }

  getAllClients(): Observable<ApiResponse<Client[]>> {
    const url = `${ApiService.BASE_URL}/clients/all`;
    return this.http.get<ApiResponse<Client[]>>(url, {
      withCredentials: true,
    });
  }

  getClientTransactions(id: string): Observable<ApiResponse<Transaction[]>> {
    const url = `${ApiService.BASE_URL}/clients/transactions/${id}`;
    return this.http.get<ApiResponse<Transaction[]>>(url, {
      withCredentials: true,
    });
  }

  getClientMotorcycle(id: string): Observable<ApiResponse<Motorcycle[]>> {
    const url = `${ApiService.BASE_URL}/clients/motorcycles/${id}`;
    return this.http.get<ApiResponse<Motorcycle[]>>(url, {
      withCredentials: true,
    });
  }

  getClientById(id: string): Observable<ApiResponse<Client>> {
    const url = `${ApiService.BASE_URL}/clients/${id}`;
    return this.http.get<ApiResponse<Client>>(url, { withCredentials: true });
  }

  deleteClient(id: string): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/clients/delete/${id}`;
    return this.http.delete<ApiResponse<null>>(url, { withCredentials: true });
  }

  /** TRANSACTION ENDPOINTS */
  purchase(body: TransactionRequest): Observable<ApiResponse<Transaction>> {
    const url = `${ApiService.BASE_URL}/transactions/purchase`;
    return this.http.post<ApiResponse<Transaction>>(url, body, {
      withCredentials: true,
    });
  }

  sell(body: TransactionRequest): Observable<ApiResponse<Transaction>> {
    const url = `${ApiService.BASE_URL}/transactions/sell`;
    return this.http.post<ApiResponse<Transaction>>(url, body, {
      withCredentials: true,
    });
  }

  returnToSupplier(body: TransactionRequest): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/transactions/return`;
    return this.http.post<ApiResponse<null>>(url, body, {
      withCredentials: true,
    });
  }

  getAllTransactions(
    page = 0,
    size = 1000,
    searchText?: string
  ): Observable<ApiResponse<Transaction[]>> {
    let url = `${ApiService.BASE_URL}/transactions/all?page=${page}&size=${size}`;
    if (searchText) {
      url += `&searchText=${encodeURIComponent(searchText)}`;
    }
    return this.http.get<ApiResponse<Transaction[]>>(url, {
      withCredentials: true,
    });
  }

  getTransactionById(id: string): Observable<ApiResponse<Transaction>> {
    const url = `${ApiService.BASE_URL}/transactions/${id}`;
    return this.http.get<ApiResponse<Transaction>>(url, {
      withCredentials: true,
    });
  }

  getTransactionsByMonthAndYear(
    month: number,
    year: number
  ): Observable<ApiResponse<Transaction[]>> {
    const url = `${ApiService.BASE_URL}/transactions/by-month-year?month=${month}&year=${year}`;
    return this.http.get<ApiResponse<Transaction[]>>(url, {
      withCredentials: true,
    });
  }

  updateTransactionStatus(
    id: string,
    status: TransactionStatus
  ): Observable<ApiResponse<null>> {
    const url = `${ApiService.BASE_URL}/transactions/update/${id}`;
    return this.http.patch<ApiResponse<null>>(url, status, {
      withCredentials: true,
    });
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
