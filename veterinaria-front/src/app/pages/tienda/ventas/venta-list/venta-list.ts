import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { VentaService } from '../services/venta';
import { Venta } from '../../../../models/venta.model';

@Component({
  selector: 'app-venta-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './venta-list.html',
  styleUrl: './venta-list.scss',
})
export class VentaList implements OnInit {
  private ventaService = inject(VentaService);

  ventas: Venta[] = [];

  // Utiliza un Set para almacenar los IDs de las filas que actualmente están expandidas.
  // Esto permite tener múltiples ventas expandidas al mismo tiempo de forma eficiente.
  expandedRows: Set<number> = new Set<number>();

  ngOnInit(): void {
    this.cargarVentas();
  }

  // Trae el historial de ventas desde el backend
  cargarVentas() {
    this.ventaService.listar().subscribe({
      next: (data) => (this.ventas = data),
      error: (err) => console.error('Error al cargar ventas', err),
    });
  }

  // Alterna el estado de expansión de una fila específica
  toggleRow(id: number) {
    if (this.expandedRows.has(id)) {
      this.expandedRows.delete(id); // Si ya está expandida, la oculta
    } else {
      this.expandedRows.add(id); // Si está oculta, la expande
    }
  }

  // Función de ayuda para la plantilla HTML: retorna 'true' si el ID está en el Set
  isExpanded(id: number): boolean {
    return this.expandedRows.has(id);
  }
}
