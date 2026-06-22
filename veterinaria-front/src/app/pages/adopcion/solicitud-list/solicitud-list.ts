import { Component, OnInit, inject } from '@angular/core';
import { AdopcionService } from '../services/adopcion';
import { SolicitudAdopcion } from '../../../models/solicitud-adopcion.model';

@Component({
  selector: 'app-solicitud-list',
  imports: [],
  templateUrl: './solicitud-list.html',
  styles: ``
})
export class SolicitudList implements OnInit {
  private adopcionService = inject(AdopcionService);
  solicitudes: SolicitudAdopcion[] = [];
  mensaje = '';
  error = '';

  ngOnInit() {
    this.cargarSolicitudes();
  }

  cargarSolicitudes() {
    this.adopcionService.listarSolicitudes().subscribe({
      next: (data) => this.solicitudes = data,
      error: () => this.solicitudes = []
    });
  }

  cambiarEstado(id: number, estado: string) {
    this.adopcionService.cambiarEstado(id, estado).subscribe({
      next: () => {
        this.mensaje = `Solicitud ${estado === 'APROBADA' ? 'aprobada' : 'rechazada'} correctamente`;
        this.error = '';
        this.cargarSolicitudes();
      },
      error: (err) => {
        this.error = err.error?.error || 'Error al actualizar la solicitud';
        this.mensaje = '';
      }
    });
  }
}
