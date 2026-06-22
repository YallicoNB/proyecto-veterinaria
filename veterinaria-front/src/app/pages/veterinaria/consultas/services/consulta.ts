import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Consulta, ConsultaRequest, AtenderConsultaRequest } from '../../../../models/consulta.model';

@Injectable({ providedIn: 'root' })
export class ConsultaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/veterinaria/consulta';

  listar(estado?: string): Observable<Consulta[]> {
    const params = estado ? `?estado=${estado}` : '';
    return this.http.get<Consulta[]>(`${this.baseUrl}${params}`);
  }

  buscarPorMascota(id: number): Observable<Consulta[]> {
    return this.http.get<Consulta[]>(`${this.baseUrl}/mascota/${id}`);
  }

  agendar(data: ConsultaRequest): Observable<Consulta> {
    return this.http.post<Consulta>(`${this.baseUrl}`, data);
  }

  atender(id: number, data: AtenderConsultaRequest): Observable<Consulta> {
    return this.http.put<Consulta>(`${this.baseUrl}/${id}/atender`, data);
  }

  cancelar(id: number): Observable<Consulta> {
    return this.http.put<Consulta>(`${this.baseUrl}/${id}/cancelar`, {});
  }
}
