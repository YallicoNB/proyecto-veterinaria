import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mascota } from '../../models/mascota.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MascotaService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/veterinaria/mascota`;

  listar(): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Mascota> {
    return this.http.get<Mascota>(`${this.baseUrl}/${id}`);
  }

  crear(data: any): Observable<Mascota> {
    return this.http.post<Mascota>(this.baseUrl, data);
  }

  actualizar(id: number, data: any): Observable<Mascota> {
    return this.http.put<Mascota>(`${this.baseUrl}/${id}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}