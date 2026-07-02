import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';
import { roleGuard } from '../../core/guards/role-guard';

export const TIENDA_ROUTES: Routes = [
  { 
    path: 'productos', 
    loadComponent: () => import('./productos/producto-list/producto-list').then(m => m.ProductoList) 
  },
  { 
    path: 'productos/nuevo', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard, roleGuard(['ADMIN'])] 
  },
  { 
    path: 'productos/:id/editar', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard, roleGuard(['ADMIN'])] 
  },
  { 
    path: 'ventas', 
    loadComponent: () => import('./ventas/venta-list/venta-list').then(m => m.VentaList), 
    canActivate: [authGuard, roleGuard(['ADMIN', 'CLIENTE_TIENDA'])] 
  },
  { 
    path: 'ventas/nueva', 
    loadComponent: () => import('./ventas/venta-form/venta-form').then(m => m.VentaForm), 
    canActivate: [authGuard, roleGuard(['ADMIN', 'CLIENTE_TIENDA'])] 
  }
];
