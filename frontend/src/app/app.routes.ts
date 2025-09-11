import { Routes } from '@angular/router';
import { GuardService } from './service/guard.service';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CategoryComponent } from './components/category/category.component';
import { SupplierComponent } from './components/supplier/supplier-list/supplier.component';
import { ProductComponent } from './components/product/product-list/product.component';
import { SupplierAddEditComponent } from './components/supplier/supplier-add-edit/supplier-add-edit.component';
import { ServiceOrderComponent } from './components/service-order/service-order-list/service-order.component';
import { ServiceOrderAddEditComponent } from './components/service-order/service-order-add-edit/service-order-add-edit.component';
import { ClientComponent } from './components/client/client-list/client.component';
import { ClientAddEditComponent } from './components/client/client-add-edit/client-add-edit.component';
import { MotorcycleComponent } from './components/motorcycle/motorcycle-list/motorcycle.component';
import { MotorcycleAddEditComponent } from './components/motorcycle/motorcycle-add-edit/motorcycle-add-edit.component';
import { ProductAddEditComponent } from './components/product/product-add-edit/product-add-edit.component';
import { PurchaseComponent } from './components/transaction/purchase/purchase.component';
import { SellComponent } from './components/transaction/sell/sell.component';
import { TransactionComponent } from './components/transaction/transaction.component';
import { TransactionDetailsComponent } from './components/transaction/transaction-details/transaction-details.component';
import { ProfileComponent } from './components/profile/profile.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: 'category',
    component: CategoryComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'supplier',
    component: SupplierComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'edit-supplier/:supplierId',
    component: SupplierAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'add-supplier',
    component: SupplierAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'service-order',
    component: ServiceOrderComponent,
    canActivate: [GuardService],
  },
  {
    path: 'edit-service-order/:serviceOrderId',
    component: ServiceOrderAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'add-service-order',
    component: ServiceOrderAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'client',
    component: ClientComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'edit-client/:clientId',
    component: ClientAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'add-client',
    component: ClientAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'motorcycle',
    component: MotorcycleComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'edit-motorcycle/:motorcycleId',
    component: MotorcycleAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'add-motorcycle',
    component: MotorcycleAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'product',
    component: ProductComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'edit-product/:productId',
    component: ProductAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },
  {
    path: 'add-product',
    component: ProductAddEditComponent,
    canActivate: [GuardService],
    data: { requiresAdmin: true },
  },

  {
    path: 'purchase',
    component: PurchaseComponent,
    canActivate: [GuardService],
  },
  { path: 'sell', component: SellComponent, canActivate: [GuardService] },

  {
    path: 'transaction',
    component: TransactionComponent,
    canActivate: [GuardService],
  },
  {
    path: 'transaction/:transactionId',
    component: TransactionDetailsComponent,
    canActivate: [GuardService],
  },

  { path: 'profile', component: ProfileComponent, canActivate: [GuardService] },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [GuardService],
  },
  {
    path: "",
    redirectTo: "/login",
    pathMatch: 'full'
  },
  {
    path: "**",
    redirectTo: "/dashboard"
  }
];
