import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../service/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-supplier-add-edit',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './supplier-add-edit.component.html',
  styleUrl: './supplier-add-edit.component.css'
})
export class SupplierAddEditComponent implements OnInit {
  constructor(private apiService:ApiService, private router:Router){}
  message:string = '';
  isEditing:boolean = false;
  supplierId:string | null = null;

  formData: any = {
    name:'',
    cnpj:'',
    contactInfo:'',
    address:''
  }

  ngOnInit(): void {
    this.supplierId = this.router.url.split('/')[2];

    if(this.supplierId) {
      this.isEditing = true;
      this.fetchSupplier();
    }
  }

  fetchSupplier():void{
    this.apiService.getSupplierById(this.supplierId!).subscribe({
      next:(res:any) =>{
        if(res.status === 200) {
          this.formData = {
            name: res.supplier.name,
            cnpj: res.supplier.cnpj,
            contactInfo: res.supplier.contactInfo,
            address: res.supplier.address
          }
        }       
      },
        error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível atualizar o fornecedor: " + error)

        }
      }      
    );
  }

  handleSubmit(){
    if (!this.formData.name || !this.formData.cnpj || !this.formData.address) {
      this.showMessage("Existem campos obrigatórios não preenchidos")
      return;
    }

    const supplierData = {
      name: this.formData.name,
      cnpj: this.formData.cnpj,
      contactInfo: this.formData.contactInfo,
      address: this.formData.address
    }

    if (this.isEditing) {
      this.apiService.updateSupplier(this.supplierId!, supplierData).subscribe({
        next:(res:any) => {
          if (res.status === 200) {
            this.showMessage("Fornecedor atualizado com sucesso")
            this.router.navigate(['/supplier']);
          }
        }, 
        error:(error) => {
            this.showMessage(error?.error?.message || error?.message || "Não foi possível atualizar o fornecedor: " + error)

          }
      })
    } else {
      this.apiService.createSupplier(supplierData).subscribe({
        next:(res:any) => {
          if (res.status === 200) {
            this.showMessage("Fornecedor adicionado com sucesso")
            this.router.navigate(['/supplier']);
          }
        }, 
        error:(error) => {
            this.showMessage(error?.error?.message || error?.message || "Não foi possível adicionar o fornecedor: " + error)

          }
      })
    }
  }

  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
    }, 4000)
  }
}
