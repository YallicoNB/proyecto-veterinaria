import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { combineLatest, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RouterLink } from '@angular/router';
import { ProductoService } from '../tienda/productos/services/producto';
import { LavanderiaService } from '../../core/services/lavanderia';
import { AdopcionService } from '../adopcion/services/adopcion';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styles: `
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 20px; margin-bottom: 24px; }
    .stat-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.08); display: flex; align-items: center; gap: 16px; }
    .stat-icon { width: 48px; height: 48px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 24px; flex-shrink: 0; }
    .stat-icon.green { background: #e8f5e9; color: #2e7d32; }
    .stat-icon.blue { background: #e3f2fd; color: #1565c0; }
    .stat-icon.orange { background: #fff3e0; color: #e65100; }
    .stat-info h3 { margin: 0; font-size: 24px; font-weight: 500; }
    .stat-info p { margin: 2px 0 0; font-size: 13px; color: rgba(0,0,0,0.54); }
    .stat-link { display: inline-block; margin-top: 6px; font-size: 13px; color: #2e7d32; text-decoration: none; }
    .stat-link:hover { text-decoration: underline; }
    .welcome p { margin: 0 0 24px; color: rgba(0,0,0,0.54); }
  `
})
export class Dashboard implements OnInit {
  private productoService = inject(ProductoService);
  private lavanderiaService = inject(LavanderiaService);
  private adopcionService = inject(AdopcionService);
  private authService = inject(AuthService);

  totalProductos = 0;
  serviciosPendientes = 0;
  mascotasDisponibles = 0;
  loading = true;
  error = '';

  get rol() { return this.authService.getRol(); }
  get username() { return this.authService.getUsername(); }

  ngOnInit() {
    combineLatest([
      this.productoService.listar().pipe(catchError(() => of([]))),
      this.lavanderiaService.pendientes().pipe(catchError(() => of([]))),
      this.adopcionService.listarDisponibles().pipe(catchError(() => of([])))
    ]).subscribe({
      next: ([productos, servicios, mascotas]) => {
        this.totalProductos = productos.length;
        this.serviciosPendientes = servicios.length;
        this.mascotasDisponibles = mascotas.length;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar los datos del dashboard';
        this.loading = false;
      }
    });
  }
}
