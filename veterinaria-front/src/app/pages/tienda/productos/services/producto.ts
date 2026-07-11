import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, ProductoRequest } from '../../../../models/producto.model';
import { environment } from '../../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/tienda/productos`;

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.baseUrl}`);
  }

  buscarPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.baseUrl}/${id}`);
  }

  bajoStock(limite: number = 10): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.baseUrl}/bajo-stock?limite=${limite}`);
  }

  crear(data: ProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(`${this.baseUrl}`, data);
  }

  actualizar(id: number, data: ProductoRequest): Observable<Producto> {
    return this.http.put<Producto>(`${this.baseUrl}/${id}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
