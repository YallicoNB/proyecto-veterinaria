import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('./pages/register/register').then(m => m.Register) },
  { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard').then(m => m.Dashboard), canActivate: [authGuard] },
  { path: 'usuarios', loadComponent: () => import('./pages/usuarios/usuario-list/usuario-list').then(m => m.UsuarioList), canActivate: [authGuard, roleGuard(['ADMIN'])] },
  { path: 'usuarios/:id/editar', loadComponent: () => import('./pages/usuarios/usuario-form/usuario-form').then(m => m.UsuarioForm), canActivate: [authGuard, roleGuard(['ADMIN'])] },

  
  { path: 'tienda/productos', loadComponent: () => import('./pages/tienda/productos/producto-list/producto-list').then(m => m.ProductoList) },
  { path: 'tienda/productos/nuevo', loadComponent: () => import('./pages/tienda/productos/producto-form/producto-form').then(m => m.ProductoForm), canActivate: [authGuard] },
  { path: 'tienda/productos/:id/editar', loadComponent: () => import('./pages/tienda/productos/producto-form/producto-form').then(m => m.ProductoForm), canActivate: [authGuard] },
  { path: 'tienda/ventas', loadComponent: () => import('./pages/tienda/ventas/venta-list/venta-list').then(m => m.VentaList), canActivate: [authGuard] },
  { path: 'tienda/ventas/nueva', loadComponent: () => import('./pages/tienda/ventas/venta-form/venta-form').then(m => m.VentaForm), canActivate: [authGuard] },

  
  {
    path: 'veterinaria',
    loadChildren: () => import('./pages/veterinaria/veterinaria-routes').then(m => m.veterinariaRoutes)
  },

  {
    path: 'lavanderia',
    loadChildren: () => import('./pages/lavanderia/lavanderia-routes').then(m => m.lavanderiaRoutes)
  },

 
  { path: 'adopcion/disponibles', loadComponent: () => import('./pages/adopcion/mascota-adoptable-list/mascota-adoptable-list').then(m => m.MascotaAdoptableList) },
  { path: 'adopcion/solicitudes/nueva', loadComponent: () => import('./pages/adopcion/solicitud-form/solicitud-form').then(m => m.SolicitudForm), canActivate: [authGuard] },
  { path: 'adopcion/solicitudes', loadComponent: () => import('./pages/adopcion/solicitud-list/solicitud-list').then(m => m.SolicitudList), canActivate: [authGuard] },

  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' },
];