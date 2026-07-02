import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Producto } from '../../../../models/producto.model';

@Component({
  selector: 'app-producto-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './producto-card.html',
  styles: [`
    .producto-card { border: 1px solid #e0e0e0; border-radius: 8px; padding: 16px; background: white; transition: box-shadow 0.2s; }
    .producto-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
    .producto-card h4 { margin: 0 0 8px; font-size: 16px; font-weight: 500; }
    .producto-card p { margin: 4px 0; font-size: 14px; color: rgba(0,0,0,0.66); }
    .producto-card button { margin-top: 8px; }
  `]
})
export class ProductoCard {
  producto = input.required<Producto>();
  esCliente = input(false);
  seleccionar = output<Producto>();
  agregarCarrito = output<Producto>();

  onSeleccionar() {
    this.seleccionar.emit(this.producto());
  }

  onAgregarCarrito() {
    this.agregarCarrito.emit(this.producto());
  }
}
