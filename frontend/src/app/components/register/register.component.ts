import { ApiService } from './../../service/api.service';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../service/api.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  constructor(private apiSerivice:ApiService, private router:Router){}

  formData: any = {
    username: '',
    password: ''
  };
  message: string | null = null;

  async handleSubmit(){
    if(
      !this.formData.username || !this.formData.password
    ){
      this.showMessage("Todos os campos são necessários")
      return;
    }

    try {
      const response: any = await firstValueFrom(this.apiSerivice.registerUser(this.formData));
      if (response.status === 200) {
        this.showMessage(response.message)
        this.router.navigate(["/login"]);
      }
    } catch (error: any) {
        console.log(error)
        this.showMessage(error?.error?.message || error?.message || "Não foi possível registrar um usuário: " + error)
    }
  }

  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {

    }, 4000)
  }
}
