import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ConsultaService } from '../../../../core/services/consulta';
import { MascotaService } from '../../../../core/services/mascota';
import { UsuarioService } from '../../../../core/services/usuario';
import { Mascota } from '../../../../models/mascota.model';
import { Usuario } from '../../../../models/usuario.model';

@Component({
  selector: 'app-consulta-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './consulta-form.html',
  styleUrl: './consulta-form.scss'
})
export class ConsultaForm implements OnInit {
  private consultaService = inject(ConsultaService);
  private mascotaService = inject(MascotaService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  mascotas: Mascota[] = [];
  veterinarios: Usuario[] = [];
  loading = false;
  error = '';

  form = new FormGroup({
    mascotaId: new FormControl<number | null>(null, Validators.required),
    veterinarioId: new FormControl<number | null>(null),
    sintomas: new FormControl('', [Validators.required, Validators.minLength(5)])
  });

  ngOnInit() {
    this.mascotaService.listar().subscribe({
      next: (data) => this.mascotas = data,
      error: () => this.error = 'Error al cargar las mascotas'
    });
    this.usuarioService.buscarPorRol('VETERINARIO').subscribe({
      next: (data) => this.veterinarios = data,
      error: () => {}
    });
  }

  get mascotaId() { return this.form.get('mascotaId'); }
  get sintomas() { return this.form.get('sintomas'); }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.consultaService.agendar({
      mascotaId: this.form.value.mascotaId!,
      veterinarioId: this.form.value.veterinarioId ?? undefined,
      sintomas: this.form.value.sintomas!
    }).subscribe({
      next: () => this.router.navigate(['/veterinaria/consultas']),
      error: () => {
        this.error = 'Error al agendar la consulta';
        this.loading = false;
      }
    });
  }
}