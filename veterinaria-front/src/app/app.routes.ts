import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.Login) },
  { path: 'register', loadComponent: () => import('./pages/register/register').then(m => m.Register) },
  { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard').then(m => m.Dashboard), canActivate: [authGuard] },
  { path: 'usuarios', loadComponent: () => import('./pages/usuarios/usuario-list/usuario-list').then(m => m.UsuarioList), canActivate: [authGuard] },

  // Tienda
  { path: 'tienda/productos', loadComponent: () => import('./pages/tienda/productos/producto-list/producto-list').then(m => m.ProductoList) },
  { path: 'tienda/productos/nuevo', loadComponent: () => import('./pages/tienda/productos/producto-form/producto-form').then(m => m.ProductoForm), canActivate: [authGuard] },
  { path: 'tienda/productos/:id/editar', loadComponent: () => import('./pages/tienda/productos/producto-form/producto-form').then(m => m.ProductoForm), canActivate: [authGuard] },
  { path: 'tienda/ventas', loadComponent: () => import('./pages/tienda/ventas/venta-list/venta-list').then(m => m.VentaList), canActivate: [authGuard] },
  { path: 'tienda/ventas/nueva', loadComponent: () => import('./pages/tienda/ventas/venta-form/venta-form').then(m => m.VentaForm), canActivate: [authGuard] },

  // Veterinaria
  { path: 'veterinaria/consultas', loadComponent: () => import('./pages/veterinaria/consultas/consulta-list/consulta-list').then(m => m.ConsultaList), canActivate: [authGuard] },
  { path: 'veterinaria/consultas/nueva', loadComponent: () => import('./pages/veterinaria/consultas/consulta-form/consulta-form').then(m => m.ConsultaForm), canActivate: [authGuard] },
  { path: 'veterinaria/consultas/:id/atender', loadComponent: () => import('./pages/veterinaria/consultas/atender-consulta/atender-consulta').then(m => m.AtenderConsulta), canActivate: [authGuard] },
  { path: 'veterinaria/vacunas', loadComponent: () => import('./pages/veterinaria/vacunas/vacuna-list/vacuna-list').then(m => m.VacunaList), canActivate: [authGuard] },
  { path: 'veterinaria/vacunas/nueva', loadComponent: () => import('./pages/veterinaria/vacunas/vacuna-form/vacuna-form').then(m => m.VacunaForm), canActivate: [authGuard] },
  { path: 'veterinaria/historia/:id', loadComponent: () => import('./pages/veterinaria/historia/historia-clinica/historia-clinica').then(m => m.HistoriaClinica), canActivate: [authGuard] },

  // Lavandería
  { path: 'lavanderia/servicios', loadComponent: () => import('./pages/lavanderia/lavanderia-list/lavanderia-list').then(m => m.LavanderiaList), canActivate: [authGuard] },
  { path: 'lavanderia/servicios/nuevo', loadComponent: () => import('./pages/lavanderia/lavanderia-form/lavanderia-form').then(m => m.LavanderiaForm), canActivate: [authGuard] },

  // Adopción
  { path: 'adopcion/disponibles', loadComponent: () => import('./pages/adopcion/mascota-adoptable-list/mascota-adoptable-list').then(m => m.MascotaAdoptableList) },
  { path: 'adopcion/solicitudes/nueva', loadComponent: () => import('./pages/adopcion/solicitud-form/solicitud-form').then(m => m.SolicitudForm), canActivate: [authGuard] },
  { path: 'adopcion/solicitudes', loadComponent: () => import('./pages/adopcion/solicitud-list/solicitud-list').then(m => m.SolicitudList), canActivate: [authGuard] },

  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' },
];
