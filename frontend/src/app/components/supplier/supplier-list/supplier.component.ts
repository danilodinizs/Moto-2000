import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-supplier',
  imports: [CommonModule],
  templateUrl: './supplier.component.html',
  styleUrl: './supplier.component.css'
})
export class SupplierComponent implements OnInit{
  constructor(private apiService:ApiService, private router:Router){}
  suppliers:any[] = [];
  message:string ='';


  ngOnInit(): void {
    this.getSuppliers();

  }

  getSuppliers():void{
    this.apiService.getAllSuppliers().subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.suppliers = res.suppliers;
        } else {
          this.showMessage(res.message)
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível salvar a categoria: " + error)

      }
    })
  }

  // navigate to add supplier page
  navigateToAddSupplierPage():void{
    this.router.navigate([`/add-supplier`]);
  }

  // navigate to add supplier page
  navigateToEditSupplierPage(supplierId: string):void{
    this.router.navigate([`/edit-supplier/${supplierId}`]);
  }

  // DELETE SUPPLIER
  deleteSupplier(supplierId: string): void {
    if (window.confirm("Você tem certeza que quer apagar esse fornecedor?"))
      this.apiService.deleteSupplier(supplierId).subscribe({
      next:(res:any) => {
        this.showMessage("Fornecedor apagado com sucesso")
        this.getSuppliers(); // reload the supplier     
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível apagar o fornecedor: " + error)

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
