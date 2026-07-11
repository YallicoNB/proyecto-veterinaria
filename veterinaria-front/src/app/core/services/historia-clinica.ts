import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistoriaClinica, HistoriaClinicaRequest } from '../../models/historia-clinica.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class HistoriaClinicaService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/veterinaria/historia`;

  buscarPorMascota(idMascota: number): Observable<HistoriaClinica[]> {
    return this.http.get<HistoriaClinica[]>(`${this.baseUrl}/${idMascota}`);
  }

  crear(data: HistoriaClinicaRequest): Observable<HistoriaClinica> {
    return this.http.post<HistoriaClinica>(this.baseUrl, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}