import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServicioLavado, ServicioLavadoRequest } from '../../../models/servicio-lavado.model';

@Injectable({ providedIn: 'root' })
export class LavanderiaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/lavanderia/servicio';

  listar(): Observable<ServicioLavado[]> {
    return this.http.get<ServicioLavado[]>(`${this.baseUrl}`);
  }

  buscarPorId(id: number): Observable<ServicioLavado> {
    return this.http.get<ServicioLavado>(`${this.baseUrl}/${id}`);
  }

  pendientes(): Observable<ServicioLavado[]> {
    return this.http.get<ServicioLavado[]>(`${this.baseUrl}/pendientes`);
  }

  crear(data: ServicioLavadoRequest): Observable<ServicioLavado> {
    return this.http.post<ServicioLavado>(`${this.baseUrl}`, data);
  }

  cambiarEstado(id: number, estado: string): Observable<ServicioLavado> {
    return this.http.patch<ServicioLavado>(`${this.baseUrl}/${id}/estado`, { estado });
  }
}
