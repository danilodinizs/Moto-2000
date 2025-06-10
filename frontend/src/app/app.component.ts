import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Observable } from 'rxjs';
import { ApiService } from './service/api.service'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Moto 2000';

  isAuthenticated$: Observable<boolean>;
  isAdmin$: Observable<boolean>;

  constructor(private apiService: ApiService) {
    this.isAuthenticated$ = this.apiService.isAuthenticated$;
    this.isAdmin$ = this.apiService.isAdmin$;
  }

  logout(): void {
    this.apiService.logout();
  }
}
