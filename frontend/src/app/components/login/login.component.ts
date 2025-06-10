
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  constructor(
    private apiService: ApiService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  formData: any = { username: '', password: '' };
  message: string | null = null;
  private returnUrl: string = '/dashboard';

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';

    this.apiService.isAuthenticated$.subscribe(isAuthenticated => {
      if (isAuthenticated) {
        this.router.navigate([this.returnUrl]);
      }
    });
  }

  async handleLogin() {
    if (!this.formData.username || !this.formData.password) {
      this.showMessage("Todos os campos são necessários");
      return;
    }

    try {
      await firstValueFrom(this.apiService.loginUser(this.formData));
      setTimeout(() => {
        this.router.navigate([this.returnUrl]);
      }, 100);

    } catch (error: any) {
      console.error("Erro no login:", error);
      this.showMessage(error?.error?.message || "Falha no login. Verifique suas credenciais.");
    }
  }

  showMessage(message: string) {
    this.message = message;
    setTimeout(() => { this.message = null; }, 4000);
  }
}
