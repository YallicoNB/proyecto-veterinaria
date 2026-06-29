import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';

export const TIENDA_ROUTES: Routes = [
  { 
    path: 'productos', 
    loadComponent: () => import('./productos/producto-list/producto-list').then(m => m.ProductoList) 
  },
  { 
    path: 'productos/nuevo', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard] 
  },
  { 
    path: 'productos/:id/editar', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard] 
  },
  { 
    path: 'ventas', 
    loadComponent: () => import('./ventas/venta-list/venta-list').then(m => m.VentaList), 
    canActivate: [authGuard] 
  },
  { 
    path: 'ventas/nueva', 
    loadComponent: () => import('./ventas/venta-form/venta-form').then(m => m.VentaForm), 
    canActivate: [authGuard] 
  }
];
