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
  expandedRows: Set<number> = new Set<number>();

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas() {
    this.ventaService.listar().subscribe({
      next: (data) => this.ventas = data,
      error: (err) => console.error('Error al cargar ventas', err)
    });
  }

  toggleRow(id: number) {
    if (this.expandedRows.has(id)) {
      this.expandedRows.delete(id);
    } else {
      this.expandedRows.add(id);
    }
  }

  isExpanded(id: number): boolean {
    return this.expandedRows.has(id);
  }
}
