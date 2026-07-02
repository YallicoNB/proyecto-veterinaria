import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdopcionService } from '../services/adopcion';
import { SolicitudAdopcion } from '../../../models/solicitud-adopcion.model';

@Component({
  selector: 'app-mis-solicitudes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-solicitudes.html',
  styleUrl: './mis-solicitudes.scss',
})
export class MisSolicitudes implements OnInit {
  private adopcionService = inject(AdopcionService);

  solicitudes: SolicitudAdopcion[] = [];
  loading = true;
  error = '';

  ngOnInit() {
    this.adopcionService.listarMisSolicitudes().subscribe({
      next: (data) => {
        this.solicitudes = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar tus solicitudes.';
        this.loading = false;
      },
    });
  }

  estadoClass(estado: string): string {
    switch (estado) {
      case 'APROBADA': return 'estado-aprobada';
      case 'RECHAZADA': return 'estado-rechazada';
      default: return 'estado-pendiente';
    }
  }

  estadoIcon(estado: string): string {
    switch (estado) {
      case 'APROBADA': return 'bi-check-circle-fill';
      case 'RECHAZADA': return 'bi-x-circle-fill';
      default: return 'bi-clock-fill';
    }
  }
}
