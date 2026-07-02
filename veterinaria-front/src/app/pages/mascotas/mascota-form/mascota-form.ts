import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MascotaService } from '../../../core/services/mascota';
import { TipoMascota, Sexo } from '../../../models/mascota.model';

@Component({
  selector: 'app-mascota-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './mascota-form.html',
  styles: [`
    .form-container { max-width: 520px; background: white; border-radius: 8px; padding: 24px; box-shadow: 0 2px 4px rgba(0,0,0,0.08); }
    .form-container h2 { margin: 0 0 24px; font-size: 20px; color: #2e7d32; }
    .form-group { display: flex; flex-direction: column; margin-bottom: 16px; }
    .form-group label { font-size: 13px; color: rgba(0,0,0,0.54); margin-bottom: 4px; }
    .form-group input, .form-group select { padding: 8px 12px; border: 1px solid #e0e0e0; border-radius: 4px; font-size: 14px; font-family: inherit; transition: border-color 0.2s; }
    .form-group input:focus, .form-group select:focus { outline: none; border-color: #2e7d32; }
    .form-group input.ng-invalid.ng-touched, .form-group select.ng-invalid.ng-touched { border-color: #f44336; }
    .form-error { color: #f44336; font-size: 12px; margin-top: 4px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
    .form-actions { display: flex; gap: 12px; margin-top: 24px; }
    .btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; text-decoration: none; transition: background 0.2s; font-family: inherit; }
    .btn-primary { background: #2e7d32; color: white; }
    .btn-primary:hover:not(:disabled) { background: #1b5e20; }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
    .btn-cancel { background: white; color: rgba(0,0,0,0.7); border: 1px solid #e0e0e0; }
    .btn-cancel:hover { background: #f5f5f5; }
    .alert { padding: 10px; border-radius: 4px; font-size: 13px; margin-bottom: 16px; }
    .alert-danger { background: #ffebee; color: #f44336; }
    .alert-info { background: #e3f2fd; color: #1565c0; }
  `]
})
export class MascotaForm implements OnInit {
  private fb = inject(FormBuilder);
  private mascotaService = inject(MascotaService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  editando = false;
  mascotaId = 0;
  loading = false;
  error = '';
  tipos = Object.values(TipoMascota);
  sexos = Object.values(Sexo);

  form = this.fb.group({
    nombre: ['', Validators.required],
    tipo: ['', Validators.required],
    raza: ['', Validators.required],
    edad: [0, [Validators.required, Validators.min(0)]],
    sexo: ['', Validators.required],
    color: [''],
    peso: [0, [Validators.required, Validators.min(0)]],
    observaciones: ['']
  });

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.editando = true;
      this.mascotaId = +idParam;
      this.loading = true;
      this.mascotaService.buscarPorId(this.mascotaId).subscribe({
        next: (m) => {
          this.form.patchValue({
            nombre: m.nombre,
            tipo: m.tipo,
            raza: m.raza,
            edad: m.edad,
            sexo: m.sexo,
            color: m.color,
            peso: m.peso,
            observaciones: m.observaciones
          });
          this.loading = false;
        },
        error: () => {
          this.error = 'Error al cargar datos de la mascota';
          this.loading = false;
        }
      });
    }
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    const data = this.form.value;

    const request = this.editando
      ? this.mascotaService.actualizar(this.mascotaId, data)
      : this.mascotaService.crear(data);

    request.subscribe({
      next: () => this.router.navigate(['/mascotas']),
      error: () => {
        this.error = 'Error al guardar mascota';
        this.loading = false;
      }
    });
  }
}
