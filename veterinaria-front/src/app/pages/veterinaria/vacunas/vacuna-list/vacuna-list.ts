import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { VacunaService } from '../../../../core/services/vacuna';
import { MascotaService } from '../../../../core/services/mascota';
import { Vacuna } from '../../../../models/vacuna.model';
import { Mascota } from '../../../../models/mascota.model';

@Component({
  selector: 'app-vacuna-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './vacuna-list.html',
  styleUrl: './vacuna-list.scss'
})
export class VacunaList implements OnInit {
  private vacunaService = inject(VacunaService);
  private mascotaService = inject(MascotaService);

  vacunas: Vacuna[] = [];
  proximas: Vacuna[] = [];
  mascotas: Mascota[] = [];
  mascotaSeleccionada: number | null = null;
  loading = false;
  error = '';

  ngOnInit() {
    this.cargarMascotas();
    this.cargarProximas();
  }

  cargarMascotas() {
    this.mascotaService.listar().subscribe({
      next: (data) => this.mascotas = data,
      error: () => this.error = 'Error al cargar las mascotas'
    });
  }

  cargarProximas() {
    this.vacunaService.proximas().subscribe({
      next: (data) => this.proximas = data,
      error: () => this.error = 'Error al cargar próximas vacunas'
    });
  }

  buscarPorMascota(idMascota: number) {
    if (!idMascota) {
      this.vacunas = [];
      this.mascotaSeleccionada = null;
      return;
    }
    this.loading = true;
    this.error = '';
    this.mascotaSeleccionada = idMascota;
    this.vacunaService.buscarPorMascota(idMascota).subscribe({
      next: (data) => {
        this.vacunas = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar las vacunas';
        this.loading = false;
      }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar esta vacuna?')) return;
    this.vacunaService.eliminar(id).subscribe({
      next: () => {
        if (this.mascotaSeleccionada) {
          this.buscarPorMascota(this.mascotaSeleccionada);
        }
        this.cargarProximas();
      },
      error: () => this.error = 'Error al eliminar la vacuna'
    });
  }

  esProxima(fechaProxima: string): boolean {
    const hoy = new Date();
    const fecha = new Date(fechaProxima);
    const diffDias = (fecha.getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24);
    return diffDias <= 30 && diffDias >= 0;
  }
}