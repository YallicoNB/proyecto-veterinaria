import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vacuna, VacunaRequest } from '../../../../models/vacuna.model';

@Injectable({ providedIn: 'root' })
export class VacunaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/veterinaria/vacuna';

  buscarPorMascota(id: number): Observable<Vacuna[]> {
    return this.http.get<Vacuna[]>(`${this.baseUrl}/mascota/${id}`);
  }

  proximas(): Observable<Vacuna[]> {
    return this.http.get<Vacuna[]>(`${this.baseUrl}/proximas`);
  }

  registrar(data: VacunaRequest): Observable<Vacuna> {
    return this.http.post<Vacuna>(`${this.baseUrl}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
