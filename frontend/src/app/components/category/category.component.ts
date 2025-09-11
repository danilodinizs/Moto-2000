import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../service/api.service';

interface Category {
  id: string,
  name: string
}

@Component({
  selector: 'app-category',
  imports: [CommonModule, FormsModule],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})

export class CategoryComponent implements OnInit {
  categories: Category[] = [];
  categoryName: string = '';
  message: string = '';
  isEditing: boolean = false;
  editingCategoryId: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.getCategories();
  }


  //GET ALL CATEGORIES
  getCategories(): void {
    this.apiService.getAllCategories().subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.categories = res.categories;
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível obter todas as categorias: " + error)

      }
    })
  }

  //ADD A NEW CATEGORY
  addCategory():void {
    if(!this.categoryName) {
      this.showMessage("O nome da categoria é obrigatório")
      return;
    }
    
    this.apiService.createCategory({name: this.categoryName}).subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.showMessage("Categoria adicionada com sucesso")
          this.categoryName = '';
          this.getCategories();
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível salvar a categoria: " + error)

      }
    })
  }

  // EDIT CATEGORY
  editCategory():void{
    if(!this.editingCategoryId || !this.categoryName) {
      return;
    }
    this.apiService.updateCategory(this.editingCategoryId, {name:this.categoryName}).subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.showMessage("Categoria atualizada com sucesso")
          this.categoryName = '';
          this.isEditing = false;
          this.getCategories();
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível atualizar a categoria: " + error)

      }
    })
  }

  //set the category to edit
  handleEditCategory(category:Category):void {
    this.isEditing = true;
    this.editingCategoryId = category.id;
    this.categoryName = category.name;
  }

  // DELETE CATEGORY
  deleteCategory(categoryId: string): void {
    if (window.confirm("Você tem certeza que quer apagar essa categoria?"))
      this.apiService.deleteCategory(categoryId).subscribe({
      next:(res:any) => {
        if (res.status === 200) {
          this.showMessage("Categoria apagada com sucesso")
          this.getCategories(); // reload the category 
        }
      }, 
      error:(error) => {
          this.showMessage(error?.error?.message || error?.message || "Não foi possível apagar a categoria: " + error)

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
