import { CommonModule } from '@angular/common';
import { ApiService } from './service/api.service';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Moto 2000';

  isAuthenticated$!: Observable<boolean>;
  isAdmin$!: Observable<boolean>;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isAuthenticated$ = this.apiService.isAuthenticated();
    this.isAdmin$ = this.apiService.isAdmin();
  }

  logout(): void {
    this.apiService.logout();
    this.router.navigate(['/login']);
    this.cdr.detectChanges();
  }
}
