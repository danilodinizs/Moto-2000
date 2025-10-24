import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PaginationComponent } from '../../pagination/pagination.component';
import { ApiService } from '../../../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product',
  imports: [CommonModule, PaginationComponent],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit{

  constructor(private apiService: ApiService, private router: Router) {}

  products: any[] = [];
  message: string = '';
  currentPage: number = 0;
  totalPages: number = 0;
  itensPerPage: number = 10;

  ngOnInit(): void {
      this.getProducts();
  }

  getProducts(): void {
    this.apiService.getAllProducts().subscribe({
      next:(res: any) => {
        const prroducts = res.products || [];
        this.totalPages = Math.ceil(this.products.length / this.itensPerPage);
        this.products = this.products.slice((this.currentPage -1) * this.itensPerPage,
        this.currentPage * this.itensPerPage);
      },
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível mostrar os produtos: " + error)

      }
    })
  }

  handleProductDelete(productId: string): void {
    if (window.confirm("Você tem certeza que quer apagar esse produto?"))
      this.apiService.deleteProduct(productId).subscribe({
      next:(res:any) => {
        this.showMessage("Produto apagado com sucesso")
        this.getProducts(); // reload the products     
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível apagar o produto: " + error)

      }
    })
  }

  onPageChange(page: number):void{
    this.currentPage = page;
    this.getProducts();
  }

  navigateToAddProduct():void {
    this.router.navigate(['/add-product'])
  }

  navigateToEditProduct(productId: string):void {
    this.router.navigate([`/edit-product/${productId}`]);
  }

  showMessage(message: string) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
    }, 4000)
  }
}
