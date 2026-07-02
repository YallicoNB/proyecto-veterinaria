import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MascotaService } from '../../../core/services/mascota';
import { Mascota } from '../../../models/mascota.model';

@Component({
  selector: 'app-mascota-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mascota-list.html',
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
    .page-header h2 { margin: 0; font-size: 22px; color: #2e7d32; }
    .btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; text-decoration: none; font-family: inherit; }
    .btn-primary { background: #2e7d32; color: white; }
    .btn-primary:hover { background: #1b5e20; }
    .btn-sm { padding: 4px 10px; font-size: 13px; }
    .btn-outline-primary { background: white; color: #2e7d32; border: 1px solid #2e7d32; }
    .btn-outline-primary:hover { background: #e8f5e9; }
    .btn-outline-danger { background: white; color: #f44336; border: 1px solid #f44336; }
    .btn-outline-danger:hover { background: #ffebee; }
    .btn-outline-info { background: white; color: #1565c0; border: 1px solid #1565c0; }
    .btn-outline-info:hover { background: #e3f2fd; }
    .table-container { background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.08); overflow: hidden; }
    .table { width: 100%; border-collapse: collapse; }
    .table th, .table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #e0e0e0; font-size: 14px; }
    .table th { background: #f5f5f5; color: rgba(0,0,0,0.54); font-weight: 600; text-transform: uppercase; font-size: 12px; }
    .table tbody tr:hover { background: #fafafa; }
    .actions { display: flex; gap: 8px; }
    .alert { padding: 10px; border-radius: 4px; font-size: 13px; margin-bottom: 16px; }
    .alert-danger { background: #ffebee; color: #f44336; }
    .alert-info { background: #e3f2fd; color: #1565c0; }
    .text-center { text-align: center; color: rgba(0,0,0,0.38); padding: 32px; }
    .badge { display: inline-block; padding: 2px 8px; border-radius: 10px; font-size: 11px; text-transform: uppercase; }
    .badge-activa { background: #e8f5e9; color: #2e7d32; }
    .badge-adoptada { background: #fff3e0; color: #e65100; }
  `]
})
export class MascotaList implements OnInit {
  private mascotaService = inject(MascotaService);

  mascotas: Mascota[] = [];
  loading = false;
  error = '';

  ngOnInit() {
    this.cargarMascotas();
  }

  cargarMascotas() {
    this.loading = true;
    this.error = '';
    this.mascotaService.listar().subscribe({
      next: (data) => {
        this.mascotas = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar mascotas.';
        this.loading = false;
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar esta mascota?')) {
      this.mascotaService.eliminar(id).subscribe({
        next: () => this.cargarMascotas(),
        error: () => this.error = 'Error al eliminar mascota.'
      });
    }
  }
}
