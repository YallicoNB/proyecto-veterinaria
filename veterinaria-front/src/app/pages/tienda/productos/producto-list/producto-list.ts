import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../services/producto';
import { Producto } from '../../../../models/producto.model';
import { FilterProductosPipe } from '../../../../pipes/filter.pipe';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, FilterProductosPipe],
  templateUrl: './producto-list.html',
  styleUrl: './producto-list.scss',
})
export class ProductoList implements OnInit {
  private productoService = inject(ProductoService);
  
  productos: Producto[] = [];
  searchText: string = '';
  mostrarBajoStock: boolean = false;

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos() {
    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos = data;
      },
      error: (err) => console.error('Error al cargar productos', err)
    });
  }

  filtrarBajoStock() {
    if (this.mostrarBajoStock) {
      this.productoService.bajoStock(10).subscribe({
        next: (data) => this.productos = data,
        error: (err) => console.error('Error al cargar bajo stock', err)
      });
    } else {
      this.cargarProductos();
    }
  }

  eliminarProducto(id: number) {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.productoService.eliminar(id).subscribe({
        next: () => {
          this.cargarProductos();
        },
        error: (err) => console.error('Error al eliminar', err)
      });
    }
  }
}
