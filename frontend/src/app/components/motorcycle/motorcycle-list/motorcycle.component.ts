import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-motorcycle',
  imports: [CommonModule],
  templateUrl: './motorcycle.component.html',
  styleUrl: './motorcycle.component.css'
})
export class MotorcycleComponent implements OnInit{
  constructor(private apiService:ApiService, private router:Router) {}
  motorcycles:any[] = [];
  message:string = '';

  ngOnInit(): void {
    this.getMotorcycles();
  }

  getMotorcycles():void {
    this.apiService.getAllMotorcycles().subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.motorcycles = res.motorcycles;
        } else {
          this.showMessage(res.message)
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível recuperar as motocicletas: " + error)

      }
    })
  }

  // navigate to add motorcycle page
  navigateToAddMotorcyclePage():void{
    this.router.navigate([`/add-motorcycle`]);
  }

  // navigate to add motorcycle page
  navigateToEditMotorcyclePage(motorcycleId: string):void{
    this.router.navigate([`/edit-motorcycle/${motorcycleId}`]);
  }

  // DELETE MOTORCYCLE
  deleteMotorcycle(motorcycleId: string): void {
    if (window.confirm("Você tem certeza que quer apagar essa motocicleta?"))
      this.apiService.deleteMotorcycle(motorcycleId).subscribe({
      next:(res:any) => {
        this.showMessage("Motocicleta apagado com sucesso")
        this.getMotorcycles(); // reload the motorcycle     
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível apagar a motocicleta: " + error)

      }
    })
  }

  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
    }, 4000)
  }
}
