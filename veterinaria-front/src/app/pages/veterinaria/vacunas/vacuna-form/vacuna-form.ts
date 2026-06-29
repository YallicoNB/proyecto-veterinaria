import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { VacunaService } from '../../../../core/services/vacuna';
import { MascotaService } from '../../../../core/services/mascota';
import { Mascota } from '../../../../models/mascota.model';

@Component({
  selector: 'app-vacuna-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './vacuna-form.html',
  styleUrl: './vacuna-form.scss'
})
export class VacunaForm implements OnInit {
  private vacunaService = inject(VacunaService);
  private mascotaService = inject(MascotaService);
  private router = inject(Router);

  mascotas: Mascota[] = [];
  loading = false;
  error = '';

  form = new FormGroup({
    mascotaId: new FormControl<number | null>(null, Validators.required),
    nombreVacuna: new FormControl('', [Validators.required, Validators.minLength(3)]),
    fechaAplicacion: new FormControl('', Validators.required),
    fechaProxima: new FormControl(''),
    lote: new FormControl('')
  });

  ngOnInit() {
    this.mascotaService.listar().subscribe({
      next: (data) => this.mascotas = data,
      error: () => this.error = 'Error al cargar las mascotas'
    });
  }

  get mascotaId() { return this.form.get('mascotaId'); }
  get nombreVacuna() { return this.form.get('nombreVacuna'); }
  get fechaAplicacion() { return this.form.get('fechaAplicacion'); }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.vacunaService.registrar({
      mascotaId: this.form.value.mascotaId!,
      nombreVacuna: this.form.value.nombreVacuna!,
      fechaAplicacion: this.form.value.fechaAplicacion ?? undefined,
      fechaProxima: this.form.value.fechaProxima ?? undefined,
      lote: this.form.value.lote ?? undefined
    }).subscribe({
      next: () => this.router.navigate(['/veterinaria/vacunas']),
      error: () => {
        this.error = 'Error al registrar la vacuna';
        this.loading = false;
      }
    });
  }
}