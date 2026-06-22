import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Venta, VentaRequest } from '../../../../models/venta.model';

@Injectable({ providedIn: 'root' })
export class VentaService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/tienda/ventas';

  listar(): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.baseUrl}`);
  }

  buscarPorId(id: number): Observable<Venta> {
    return this.http.get<Venta>(`${this.baseUrl}/${id}`);
  }

  registrar(data: VentaRequest): Observable<Venta> {
    return this.http.post<Venta>(`${this.baseUrl}`, data);
  }
}
