import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'barApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'poste',
        data: { pageTitle: 'barApp.poste.home.title' },
        loadChildren: () => import('./poste/poste.module').then(m => m.PosteModule),
      },
      {
        path: 'compte-caisse',
        data: { pageTitle: 'barApp.poste.home.title' },
        loadChildren: () => import('./compte-caisse/compte-caisse.module').then(m => m.CompteCaisseModule),
      },
      {
        path: 'shift',
        data: { pageTitle: 'barApp.shift.home.title' },
        loadChildren: () => import('./shift/shift.module').then(m => m.ShiftModule),
      },
      {
        path: 'attendance',
        data: { pageTitle: 'barApp.attendance.home.title' },
        loadChildren: () => import('./attendance/attendance.module').then(m => m.AttendanceModule),
      },
      {
        path: 'salary',
        data: { pageTitle: 'barApp.salary.home.title' },
        loadChildren: () => import('./salary/salary.module').then(m => m.SalaryModule),
      },
      {
        path: 'leave-request',
        data: { pageTitle: 'barApp.leaveRequest.home.title' },
        loadChildren: () => import('./leave-request/leave-request.module').then(m => m.LeaveRequestModule),
      },
      {
        path: 'boisson',
        data: { pageTitle: 'barApp.boisson.home.title' },
        loadChildren: () => import('./boisson/boisson.module').then(m => m.BoissonModule),
      },

      {
        path: 'client',
        data: { pageTitle: 'barApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'depot',
        data: { pageTitle: 'barApp.depot.home.title' },
        loadChildren: () => import('./depot/depot.module').then(m => m.DepotModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'barApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'barApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-price',
        data: { pageTitle: 'barApp.productPrice.home.title' },
        loadChildren: () => import('./product-price/product-price.module').then(m => m.ProductPriceModule),
      },
      {
        path: 'stock',
        data: { pageTitle: 'barApp.stock.home.title' },
        loadChildren: () => import('./stock/stock.module').then(m => m.StockModule),
      },
      {
        path: 'sale',
        data: { pageTitle: 'barApp.sale.home.title' },
        loadChildren: () => import('./sale/sale.module').then(m => m.SaleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
