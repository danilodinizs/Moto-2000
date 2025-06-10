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

  // MUDANÇA 1: O tipo de retorno agora é um Observable<boolean>
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> {

    const requiresAdmin = route.data['requiresAdmin'] || false;

    if (requiresAdmin) {
      // MUDANÇA 2: Usamos o observable isAdmin$ e o operador pipe()
      return this.apiService.isAdmin$.pipe(
        take(1), // Pega o primeiro valor emitido e encerra a inscrição, evitando problemas de memória
        map(isAdmin => {
          if (isAdmin) {
            return true; // Se for admin, permite o acesso
          }
          // Se não for admin, redireciona para o login e bloqueia a rota
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return false;
        })
      );
    } else {
      // MUDANÇA 3: Lógica similar para rotas que exigem apenas autenticação
      return this.apiService.isAuthenticated$.pipe(
        take(1),
        map(isAuthenticated => {
          if (isAuthenticated) {
            return true; // Se estiver autenticado, permite o acesso
          }
          // Se não estiver autenticado, redireciona e bloqueia
          this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          return false;
        })
      );
    }
  }
}
