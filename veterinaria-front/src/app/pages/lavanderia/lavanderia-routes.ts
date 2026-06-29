import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';
import { roleGuard } from '../../core/guards/role-guard';

export const lavanderiaRoutes: Routes = [
  {
    path: 'servicios',
    loadComponent: () => import('./lavanderia-list/lavanderia-list').then(m => m.LavanderiaList),
    canActivate: [authGuard, roleGuard(['ADMIN', 'EMPLEADO_LAVANDERIA'])]
  },
  {
    path: 'servicios/nuevo',
    loadComponent: () => import('./lavanderia-form/lavanderia-form').then(m => m.LavanderiaForm),
    canActivate: [authGuard, roleGuard(['ADMIN', 'EMPLEADO_LAVANDERIA'])]
  }
];