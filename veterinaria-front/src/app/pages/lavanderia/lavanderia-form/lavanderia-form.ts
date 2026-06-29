import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { LavanderiaService } from '../../../core/services/lavanderia';
import { MascotaService } from '../../../core/services/mascota';
import { Mascota } from '../../../models/mascota.model';
import { TipoServicio } from '../../../models/servicio-lavado.model';

@Component({
  selector: 'app-lavanderia-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './lavanderia-form.html',
  styleUrl: './lavanderia-form.scss'
})
export class LavanderiaForm implements OnInit {
  private lavanderiaService = inject(LavanderiaService);
  private mascotaService = inject(MascotaService);
  private router = inject(Router);

  mascotas: Mascota[] = [];
  tiposServicio = Object.values(TipoServicio);
  loading = false;
  error = '';

  form = new FormGroup({
    mascotaId: new FormControl<number | null>(null, Validators.required),
    tipoServicio: new FormControl<TipoServicio | null>(null, Validators.required),
    precio: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    fechaHora: new FormControl('', Validators.required),
    observaciones: new FormControl('')
  });

  ngOnInit() {
    this.mascotaService.listar().subscribe({
      next: (data) => this.mascotas = data,
      error: () => this.error = 'Error al cargar las mascotas'
    });
  }

  get mascotaId() { return this.form.get('mascotaId'); }
  get tipoServicio() { return this.form.get('tipoServicio'); }
  get precio() { return this.form.get('precio'); }
  get fechaHora() { return this.form.get('fechaHora'); }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.lavanderiaService.crear({
      mascotaId: this.form.value.mascotaId!,
      tipoServicio: this.form.value.tipoServicio!,
      precio: this.form.value.precio!,
      fechaHora: this.form.value.fechaHora!,
      observaciones: this.form.value.observaciones ?? undefined
    }).subscribe({
      next: () => this.router.navigate(['/lavanderia/servicios']),
      error: () => {
        this.error = 'Error al crear el servicio';
        this.loading = false;
      }
    });
  }
}