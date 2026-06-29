import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ConsultaService } from '../../../../core/services/consulta';
import { Consulta } from '../../../../models/consulta.model';

@Component({
  selector: 'app-atender-consulta',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './atender-consulta.html',
  styleUrl: './atender-consulta.scss'
})
export class AtenderConsulta implements OnInit {
  private consultaService = inject(ConsultaService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  consulta: Consulta | null = null;
  loading = false;
  error = '';
  id!: number;

  form = new FormGroup({
    diagnostico: new FormControl('', [Validators.required, Validators.minLength(5)]),
    receta: new FormControl('')
  });

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.consultaService.listar().subscribe({
      next: (data) => {
        this.consulta = data.find(c => c.id === this.id) ?? null;
        if (!this.consulta) this.error = 'Consulta no encontrada';
      },
      error: () => this.error = 'Error al cargar la consulta'
    });
  }

  get diagnostico() { return this.form.get('diagnostico'); }
  get receta() { return this.form.get('receta'); }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.consultaService.atender(this.id, {
      diagnostico: this.form.value.diagnostico!,
      receta: this.form.value.receta ?? ''
    }).subscribe({
      next: () => this.router.navigate(['/veterinaria/consultas']),
      error: () => {
        this.error = 'Error al atender la consulta';
        this.loading = false;
      }
    });
  }
}