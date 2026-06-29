import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Consulta, ConsultaRequest, AtenderConsultaRequest, EstadoConsulta } from '../../models/consulta.model';

@Injectable({ providedIn: 'root' })
export class ConsultaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/veterinaria/consulta';

  listar(estado?: EstadoConsulta): Observable<Consulta[]> {
    let params = new HttpParams();
    if (estado) {
      params = params.set('estado', estado);
    }
    return this.http.get<Consulta[]>(this.baseUrl, { params });
  }

  buscarPorMascota(idMascota: number): Observable<Consulta[]> {
    return this.http.get<Consulta[]>(`${this.baseUrl}/mascota/${idMascota}`);
  }

  agendar(data: ConsultaRequest): Observable<Consulta> {
    return this.http.post<Consulta>(this.baseUrl, data);
  }

  atender(id: number, data: AtenderConsultaRequest): Observable<Consulta> {
    return this.http.put<Consulta>(`${this.baseUrl}/${id}/atender`, data);
  }

  cancelar(id: number): Observable<Consulta> {
    return this.http.put<Consulta>(`${this.baseUrl}/${id}/cancelar`, {});
  }
}