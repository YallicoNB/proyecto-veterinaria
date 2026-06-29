import { Component, OnInit, inject } from '@angular/core';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { HistoriaClinicaService } from '../../../../core/services/historia-clinica';
import { MascotaService } from '../../../../core/services/mascota';

import { HistoriaClinica as HistoriaClinicaModel } from '../../../../models/historia-clinica.model';
import { Mascota } from '../../../../models/mascota.model';

@Component({
  selector: 'app-historia-clinica',
  imports: [RouterLink, ReactiveFormsModule, DatePipe],
  templateUrl: './historia-clinica.html',
  styleUrl: './historia-clinica.scss'
})
export class HistoriaClinica implements OnInit {
  private historiaService = inject(HistoriaClinicaService);
  private mascotaService = inject(MascotaService);
  private route = inject(ActivatedRoute);

  historias: HistoriaClinicaModel[] = [];
  mascota: Mascota | null = null;
  idMascota!: number;
  loading = false;
  error = '';
  mostrarFormulario = false;

  form = new FormGroup({
    motivoConsulta: new FormControl('', [Validators.required, Validators.minLength(5)]),
    diagnostico: new FormControl(''),
    tratamiento: new FormControl('')
  });

  ngOnInit() {
    this.idMascota = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarMascota();
    this.cargarHistorias();
  }

  cargarMascota() {
    this.mascotaService.buscarPorId(this.idMascota).subscribe({
      next: (data) => this.mascota = data,
      error: () => this.error = 'Error al cargar la mascota'
    });
  }

  cargarHistorias() {
    this.loading = true;
    this.error = '';
    this.historiaService.buscarPorMascota(this.idMascota).subscribe({
      next: (data) => {
        this.historias = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar el historial';
        this.loading = false;
      }
    });
  }

  get motivoConsulta() { return this.form.get('motivoConsulta'); }

  toggleFormulario() {
    this.mostrarFormulario = !this.mostrarFormulario;
    if (!this.mostrarFormulario) this.form.reset();
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.historiaService.crear({
      mascotaId: this.idMascota,
      motivoConsulta: this.form.value.motivoConsulta!,
      diagnostico: this.form.value.diagnostico ?? undefined,
      tratamiento: this.form.value.tratamiento ?? undefined
    }).subscribe({
      next: () => {
        this.form.reset();
        this.mostrarFormulario = false;
        this.cargarHistorias();
      },
      error: () => {
        this.error = 'Error al guardar el registro';
        this.loading = false;
      }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este registro del historial?')) return;
    this.historiaService.eliminar(id).subscribe({
      next: () => this.cargarHistorias(),
      error: () => this.error = 'Error al eliminar el registro'
    });
  }
}