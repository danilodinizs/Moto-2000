import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class GuardService implements CanActivate {

  constructor(private apiService: ApiService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> {

    const requiresAdmin = route.data['requiresAdmin'] || false;

    return this.apiService.isAuthenticated$.pipe(
      take(1),
      switchMap(isAuthenticated => {
        if (!isAuthenticated) {
          // Se não estiver autenticado, redireciona para login
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return [false];
        }

        if (requiresAdmin) {
          // Se requer admin, verifica se é admin
          return this.apiService.isAdmin$.pipe(
            take(1),
            map(isAdmin => {
              if (isAdmin) {
                return true;
              }
              // Se não for admin mas estiver autenticado, redireciona para dashboard
              this.router.navigate(['/dashboard']);
              return false;
            })
          );
        } else {
          // Se não requer admin e está autenticado, permite acesso
          return [true];
        }
      })
    );
  }
}

