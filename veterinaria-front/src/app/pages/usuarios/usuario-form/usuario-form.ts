import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario';
import { Rol } from '../../../models/usuario.model';

@Component({
  selector: 'app-usuario-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './usuario-form.html',
  styles: [`
    .form-container { max-width: 480px; background: white; border-radius: 8px; padding: 24px; box-shadow: 0 2px 4px rgba(0,0,0,0.08); }
    .form-group { display: flex; flex-direction: column; margin-bottom: 16px; }
    .form-group label { font-size: 13px; color: rgba(0,0,0,0.54); margin-bottom: 4px; }
    .form-group input, .form-group select { padding: 8px 12px; border: 1px solid #e0e0e0; border-radius: 4px; font-size: 14px; font-family: inherit; transition: border-color 0.2s; }
    .form-group input:focus, .form-group select:focus { outline: none; border-color: #2e7d32; }
    .form-group input.ng-invalid.ng-touched, .form-group select.ng-invalid.ng-touched { border-color: #f44336; }
    .form-error { color: #f44336; font-size: 12px; margin-top: 4px; }
    .form-actions { display: flex; gap: 12px; margin-top: 24px; }
    .btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; text-decoration: none; transition: background 0.2s; font-family: inherit; }
    .btn-primary { background: #2e7d32; color: white; }
    .btn-primary:hover:not(:disabled) { background: #1b5e20; }
    .btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }
    .btn-cancel { background: white; color: rgba(0,0,0,0.7); border: 1px solid #e0e0e0; }
    .btn-cancel:hover { background: #f5f5f5; }
    .alert { padding: 10px; border-radius: 4px; font-size: 13px; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; }
    .alert-success { background: #e8f5e9; color: #2e7d32; }
    .alert-error { background: #ffebee; color: #f44336; }
    .spinner { display: inline-block; width: 16px; height: 16px; border: 2px solid rgba(255,255,255,0.3); border-top-color: white; border-radius: 50%; animation: spin 0.6s linear infinite; }
    @keyframes spin { to { transform: rotate(360deg); } }
  `]
})
export class UsuarioForm implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private usuarioService = inject(UsuarioService);

  editando = false;
  userId = 0;
  loading = false;
  cargandoDatos = false;
  error = '';
  success = '';
  roles = Object.values(Rol);

  form = this.fb.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.minLength(6)]],
    rol: ['', Validators.required]
  });

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.editando = true;
      this.userId = +idParam;
      this.cargandoDatos = true;
      this.usuarioService.buscarPorId(this.userId).subscribe({
        next: (u) => {
          this.form.patchValue({
            username: u.username,
            email: u.email,
            rol: u.rol
          });
          this.form.get('password')?.clearValidators();
          this.form.get('password')?.updateValueAndValidity();
          this.cargandoDatos = false;
        },
        error: () => {
          this.error = 'Error al cargar datos del usuario';
          this.cargandoDatos = false;
        }
      });
    }
  }

  submit() {
    if (this.form.invalid || this.cargandoDatos) return;
    this.loading = true;
    this.error = '';
    this.success = '';

    const data = this.form.value as any;
    if (!data.password) delete data.password;

    const request = this.editando
      ? this.usuarioService.actualizar(this.userId, data)
      : this.usuarioService.crear(data);

    request.subscribe({
      next: () => {
        this.success = this.editando ? 'Usuario actualizado' : 'Usuario creado';
        setTimeout(() => this.router.navigate(['/usuarios']), 1500);
      },
      error: (err) => {
        this.error = err.error?.error || 'Error al guardar usuario';
        this.loading = false;
      }
    });
  }
}
