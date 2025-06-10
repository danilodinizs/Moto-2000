import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class GuardService implements CanActivate {

  constructor(private apiService: ApiService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> {

    const requiresAdmin = route.data['requiresAdmin'] || false;

    if (requiresAdmin) {
      return this.apiService.isAdmin$.pipe(
        take(1),
        map(isAdmin => {
          if (isAdmin) {
            return true;
          }
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return false;
        })
      );
    } else {
      return this.apiService.isAuthenticated$.pipe(
        take(1),
        map(isAuthenticated => {
          if (isAuthenticated) {
            return true;
          }

          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return false;
        })
      );
    }
  }
}
