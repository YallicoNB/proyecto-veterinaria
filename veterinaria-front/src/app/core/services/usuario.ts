import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario, UsuarioRequest } from '../../models/usuario.model';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/usuarios';

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}`);
  }

  buscarPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
  }

  buscarPorRol(rol: string): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.baseUrl}/buscar?rol=${rol}`);
  }

  crear(data: UsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseUrl}/registro`, data);
  }

  actualizar(id: number, data: UsuarioRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}`, data);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
