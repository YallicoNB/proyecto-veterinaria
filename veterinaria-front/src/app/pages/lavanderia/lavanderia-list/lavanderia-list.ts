import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { LavanderiaService } from '../../../core/services/lavanderia';
import { ServicioLavado } from '../../../models/servicio-lavado.model';

@Component({
  selector: 'app-lavanderia-list',
  imports: [RouterLink, DatePipe, CurrencyPipe],
  templateUrl: './lavanderia-list.html',
  styleUrl: './lavanderia-list.scss'
})
export class LavanderiaList implements OnInit {
  private lavanderiaService = inject(LavanderiaService);

  servicios: ServicioLavado[] = [];
  serviciosFiltrados: ServicioLavado[] = [];
  loading = false;
  error = '';
  soloPendientes = false;

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.loading = true;
    this.error = '';
    this.lavanderiaService.listar().subscribe({
      next: (data) => {
        this.servicios = data;
        this.aplicarFiltro();
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar los servicios';
        this.loading = false;
      }
    });
  }

  aplicarFiltro() {
    this.serviciosFiltrados = this.soloPendientes
      ? this.servicios.filter(s => s.estado === 'PENDIENTE')
      : [...this.servicios];
  }

  togglePendientes() {
    this.soloPendientes = !this.soloPendientes;
    this.aplicarFiltro();
  }

  cambiarEstado(id: number, estado: string) {
    this.lavanderiaService.cambiarEstado(id, estado).subscribe({
      next: () => this.cargar(),
      error: () => this.error = 'Error al cambiar el estado'
    });
  }

  getBadgeClass(estado: string): string {
    const clases: Record<string, string> = {
      PENDIENTE: 'badge-pendiente',
      EN_PROCESO: 'badge-proceso',
      COMPLETADO: 'badge-completado',
      CANCELADO: 'badge-cancelado'
    };
    return clases[estado] ?? '';
  }

  getSiguienteEstado(estado: string): string {
    const siguientes: Record<string, string> = {
      PENDIENTE: 'EN_PROCESO',
      EN_PROCESO: 'COMPLETADO'
    };
    return siguientes[estado] ?? '';
  }

  getSiguienteEstadoLabel(estado: string): string {
    const labels: Record<string, string> = {
      PENDIENTE: 'Iniciar',
      EN_PROCESO: 'Completar'
    };
    return labels[estado] ?? '';
  }
}