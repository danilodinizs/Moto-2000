import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, map, of, tap, throwError } from 'rxjs';
import { Category } from '../interfaces/category';
import { ApiResponse } from '../interfaces/api-response';


// Interface para o status de autenticação retornado pelo backend
interface AuthStatus {
  isAuthenticated: boolean;
  role: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private static BASE_URL = 'http://localhost:2904/v1/api'; // Mantenha sua URL base

  authStatusChange = new EventEmitter<void>();

  constructor(private http: HttpClient) { }


  private getAuthStatus(): Observable<AuthStatus> {
    const url = `${ApiService.BASE_URL}/auth/status`; // Endpoint a ser criado no backend
    return this.http.get<AuthStatus>(url, { withCredentials: true }).pipe(
      catchError(error => {
        console.error('Erro ao buscar status de autenticação:', error);
        return of({ isAuthenticated: false, role: null });
      })
    );
  }


  isAuthenticated(): Observable<boolean> {
    return this.getAuthStatus().pipe(
      map(status => status.isAuthenticated)
    );
  }

  isAdmin(): Observable<boolean> {
    return this.getAuthStatus().pipe(
      map(status => status.isAuthenticated && status.role === 'MANAGER')
    );
  }


/***AUTH & USERS API METHODS */

  registerUser(body: any): Observable<any> {
    const url = `${ApiService.BASE_URL}/auth/register`;
    return this.http.post(url, body);
  }

  loginUser(body: any): Observable<any> {
    const url = `${ApiService.BASE_URL}/auth/login`;
    return this.http.post(url, { withCredentials: true });
  }

  logout(): Observable<any> {
    const url = `${ApiService.BASE_URL}/auth/logout`;
    return this.http.post(url, {}, { withCredentials: true }).pipe(
      tap(() => {
        this.authStatusChange.emit();
      }),
      catchError(error => {
        console.error('Erro ao fazer logout:', error);
        this.authStatusChange.emit();
        return throwError(() => error);
      })
    );
  }

  getLoggedInUserInfo(): Observable<any> {
    const url = `${ApiService.BASE_URL}/users/current`;
    return this.http.get<any>(url, { withCredentials: true } );
}

  /**CATEGOTY ENDPOINTS */
createCategory(body: Category): Observable<ApiResponse<Category>> {
const url = `${ApiService.BASE_URL}/categories/add`;
return this.http.post<ApiResponse<Category>>(url, body, { withCredentials: true });
}

getAllCategories(): Observable<ApiResponse<Category[]>> {
  const url = `${ApiService.BASE_URL}/categories/all`;
  return this.http.get<ApiResponse<Category[]>>(url, { withCredentials: true });
}

getCategoryById(id: string): Observable<ApiResponse<Category>> {
  const url = `${ApiService.BASE_URL}/categories/${id}`;
  return this.http.get<ApiResponse<Category>>(url, { withCredentials: true });
}

updateCategory(id: string, body: Partial<Category>): Observable<ApiResponse<Category>> {
  const url = `${ApiService.BASE_URL}/categories/update/${id}`;
  return this.http.patch<ApiResponse<Category>>(url, body, { withCredentials: true });
}

deleteCategory(id: string): Observable<ApiResponse<null>> {
  const url = `${ApiService.BASE_URL}/categories/delete/${id}`;
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
