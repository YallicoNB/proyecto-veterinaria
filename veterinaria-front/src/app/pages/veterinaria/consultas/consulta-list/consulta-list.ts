import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ConsultaService } from '../../../../core/services/consulta';
import { Consulta, EstadoConsulta } from '../../../../models/consulta.model';

@Component({
  selector: 'app-consulta-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './consulta-list.html',
  styleUrl: './consulta-list.scss'
})
export class ConsultaList implements OnInit {
  private consultaService = inject(ConsultaService);

  consultas: Consulta[] = [];
  filtroEstado: EstadoConsulta | undefined = undefined;
  loading = false;
  error = '';

  readonly estados = Object.values(EstadoConsulta);

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.loading = true;
    this.error = '';
    this.consultaService.listar(this.filtroEstado).subscribe({
      next: (data) => {
        this.consultas = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error al cargar las consultas';
        this.loading = false;
      }
    });
  }

  cambiarFiltro(estado: string) {
    this.filtroEstado = estado ? estado as EstadoConsulta : undefined;
    this.cargar();
  }

  cancelar(id: number) {
    if (!confirm('¿Cancelar esta consulta?')) return;
    this.consultaService.cancelar(id).subscribe({
      next: () => this.cargar(),
      error: () => this.error = 'Error al cancelar la consulta'
    });
  }

  getBadgeClass(estado: EstadoConsulta): string {
    const clases: Record<EstadoConsulta, string> = {
      PENDIENTE: 'badge-pendiente',
      REALIZADA: 'badge-realizada',
      CANCELADA: 'badge-cancelada'
    };
    return clases[estado];
  }
}