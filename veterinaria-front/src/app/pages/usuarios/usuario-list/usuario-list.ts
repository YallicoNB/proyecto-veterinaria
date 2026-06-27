import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario';
import { Usuario } from '../../../models/usuario.model';

@Component({
  selector: 'app-usuario-list',
  imports: [RouterLink],
  templateUrl: './usuario-list.html',
  styles: [`
    .toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
    .btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; text-decoration: none; transition: background 0.2s; }
    .btn-primary { background: #2e7d32; color: white; }
    .btn-primary:hover { background: #1b5e20; }
    .btn-sm { padding: 5px 10px; font-size: 12px; }
    .btn-warn { background: #f44336; color: white; }
    .btn-warn:hover { background: #c62828; }
    .btn-outline { background: white; color: rgba(0,0,0,0.7); border: 1px solid #e0e0e0; }
    .btn-outline:hover { background: #f5f5f5; }
    .spinner { text-align: center; padding: 60px; color: rgba(0,0,0,0.4); }
    .empty-state { text-align: center; padding: 40px; color: rgba(0,0,0,0.54); font-size: 16px; }
    .alert { padding: 10px; border-radius: 4px; font-size: 13px; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; }
    .alert-success { background: #e8f5e9; color: #2e7d32; }
    .alert-error { background: #ffebee; color: #f44336; }
    .badge { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 12px; font-weight: 500; }
    .badge-active { background: #e8f5e9; color: #2e7d32; }
    .badge-inactive { background: #ffebee; color: #c62828; }
  `]
})
export class UsuarioList implements OnInit {
  private usuarioService = inject(UsuarioService);

  usuarios: Usuario[] = [];
  loading = true;
  error = '';
  mensaje = '';

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.loading = true;
    this.usuarioService.listar().subscribe({
      next: (data) => { this.usuarios = data; this.loading = false; },
      error: () => { this.error = 'Error al cargar usuarios'; this.loading = false; }
    });
  }

  eliminar(id: number, username: string) {
    if (!confirm(`¿Eliminar al usuario "${username}"?`)) return;
    this.usuarioService.eliminar(id).subscribe({
      next: () => {
        this.mensaje = `Usuario "${username}" eliminado`;
        this.cargar();
      },
      error: (err) => {
        this.error = err.error?.error || 'Error al eliminar usuario';
      }
    });
  }
}
