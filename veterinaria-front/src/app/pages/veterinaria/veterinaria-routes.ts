import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';
import { roleGuard } from '../../core/guards/role-guard';

export const veterinariaRoutes: Routes = [
  {
    path: 'consultas',
    loadComponent: () => import('./consultas/consulta-list/consulta-list').then(m => m.ConsultaList),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  },
  {
    path: 'consultas/nueva',
    loadComponent: () => import('./consultas/consulta-form/consulta-form').then(m => m.ConsultaForm),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  },
  {
    path: 'consultas/:id/atender',
    loadComponent: () => import('./consultas/atender-consulta/atender-consulta').then(m => m.AtenderConsulta),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  },
  {
    path: 'vacunas',
    loadComponent: () => import('./vacunas/vacuna-list/vacuna-list').then(m => m.VacunaList),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  },
  {
    path: 'vacunas/nueva',
    loadComponent: () => import('./vacunas/vacuna-form/vacuna-form').then(m => m.VacunaForm),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  },
  {
    path: 'historia/:id',
    loadComponent: () => import('./historia/historia-clinica/historia-clinica').then(m => m.HistoriaClinica),
    canActivate: [authGuard, roleGuard(['ADMIN', 'VETERINARIO'])]
  }
];