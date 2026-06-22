import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AdopcionService } from '../services/adopcion';

@Component({
  selector: 'app-solicitud-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './solicitud-form.html',
  styles: ``
})
export class SolicitudForm implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private adopcionService = inject(AdopcionService);

  mensaje = '';
  error = '';
  enviado = false;

  form = this.fb.group({
    mascotaId: [0, Validators.required],
    nombreSolicitante: ['', Validators.required],
    telefono: ['', [Validators.required, Validators.pattern('^[0-9]{7,15}$')]],
    motivo: ['', Validators.required]
  });

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['mascotaId']) {
        this.form.patchValue({ mascotaId: +params['mascotaId'] });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;
    this.enviado = true;
    this.mensaje = '';
    this.error = '';
    this.adopcionService.enviarSolicitud(this.form.value as any).subscribe({
      next: () => {
        this.mensaje = 'Solicitud enviada correctamente';
        setTimeout(() => this.router.navigate(['/adopcion/disponibles']), 1500);
      },
      error: (err) => {
        this.error = err.error?.error || 'Error al enviar la solicitud';
        this.enviado = false;
      }
    });
  }
}
