import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';

// Definición de las rutas hijas para el módulo Tienda
export const TIENDA_ROUTES: Routes = [
  { 
    // Ruta base de productos: /tienda/productos
    path: 'productos', 
    loadComponent: () => import('./productos/producto-list/producto-list').then(m => m.ProductoList) 
  },
  { 
    // Ruta para crear producto: /tienda/productos/nuevo
    path: 'productos/nuevo', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard] // Protegido por guard de autenticación
  },
  { 
    // Ruta para editar producto existente (recibe el parámetro :id)
    path: 'productos/:id/editar', 
    loadComponent: () => import('./productos/producto-form/producto-form').then(m => m.ProductoForm), 
    canActivate: [authGuard] 
  },
  { 
    // Ruta base de ventas: /tienda/ventas
    path: 'ventas', 
    loadComponent: () => import('./ventas/venta-list/venta-list').then(m => m.VentaList), 
    canActivate: [authGuard] 
  },
  { 
    // Ruta para registrar nueva venta
    path: 'ventas/nueva', 
    loadComponent: () => import('./ventas/venta-form/venta-form').then(m => m.VentaForm), 
    canActivate: [authGuard] 
  }
];
