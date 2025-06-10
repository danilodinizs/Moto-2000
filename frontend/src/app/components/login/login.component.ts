import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  constructor(private apiService: ApiService, private router: Router) {}

  formData: any = { username: '', password: '' };
  message: string | null = null;

  async handleLogin() {
    if (!this.formData.username || !this.formData.password) {
      this.showMessage("Todos os campos são necessários");
      return;
    }

    try {
      await firstValueFrom(this.apiService.loginUser(this.formData));

      this.router.navigate(["/dashboard"]);

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
