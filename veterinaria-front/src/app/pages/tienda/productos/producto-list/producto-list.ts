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
  errorMessage: string = '';
  loading: boolean = false;

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos() {
    this.loading = true;
    this.errorMessage = '';
    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Error al cargar productos.';
        this.loading = false;
      }
    });
  }

  filtrarBajoStock() {
    this.errorMessage = '';
    if (this.mostrarBajoStock) {
      this.loading = true;
      this.productoService.bajoStock(10).subscribe({
        next: (data) => {
          this.productos = data;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Error al filtrar por bajo stock.';
          this.loading = false;
        }
      });
    } else {
      this.cargarProductos();
    }
  }

  eliminarProducto(id: number) {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.errorMessage = '';
      this.productoService.eliminar(id).subscribe({
        next: () => {
          this.cargarProductos();
        },
        error: () => {
          this.errorMessage = 'Error al eliminar el producto.';
        }
      });
    }
  }
}
