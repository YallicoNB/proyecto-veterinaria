import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mascota } from '../../models/mascota.model';

@Injectable({ providedIn: 'root' })
export class MascotaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/veterinaria/mascota';

  listar(): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<Mascota> {
    return this.http.get<Mascota>(`${this.baseUrl}/${id}`);
  }
}