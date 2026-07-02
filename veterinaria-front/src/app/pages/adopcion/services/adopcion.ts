import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MascotaAdoptable } from '../../../models/mascota-adoptable.model';
import { SolicitudAdopcion, SolicitudAdopcionRequest } from '../../../models/solicitud-adopcion.model';

@Injectable({ providedIn: 'root' })
export class AdopcionService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/adopcion';

  listarSolicitudes(): Observable<SolicitudAdopcion[]> {
    return this.http.get<SolicitudAdopcion[]>(`${this.baseUrl}/solicitudes`);
  }

  listarMisSolicitudes(): Observable<SolicitudAdopcion[]> {
    return this.http.get<SolicitudAdopcion[]>(`${this.baseUrl}/mis-solicitudes`);
  }

  listarDisponibles(): Observable<MascotaAdoptable[]> {
    return this.http.get<MascotaAdoptable[]>(`${this.baseUrl}/disponibles`);
  }

  enviarSolicitud(data: SolicitudAdopcionRequest): Observable<SolicitudAdopcion> {
    return this.http.post<SolicitudAdopcion>(`${this.baseUrl}/solicitudes`, data);
  }

  cambiarEstado(id: number, estado: string): Observable<SolicitudAdopcion> {
    return this.http.patch<SolicitudAdopcion>(`${this.baseUrl}/solicitudes/${id}/estado?estado=${estado}`, {});
  }
}
