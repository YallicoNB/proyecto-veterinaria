import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../services/producto';
import { Producto } from '../../../../models/producto.model';
import { FilterProductosPipe } from '../../../../pipes/filter.pipe';
import { ProductoCard } from '../producto-card/producto-card';
import { AuthService } from '../../../../core/services/auth';
import { CartService } from '../../../../core/services/cart';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, FilterProductosPipe, ProductoCard],
  templateUrl: './producto-list.html',
  styleUrl: './producto-list.scss',
})
export class ProductoList implements OnInit {
  private productoService = inject(ProductoService);
  private authService = inject(AuthService);
  private cartService = inject(CartService);
  
  productos: Producto[] = [];
  searchText: string = '';
  idSearchText: string = '';
  mostrarBajoStock: boolean = false;
  errorMessage: string = '';
  loading: boolean = false;
  selectedProducto: Producto | null = null;

  get esAdmin(): boolean {
    return this.authService.getRol() === 'ADMIN';
  }

  get esCliente(): boolean {
    return this.authService.getRol() === 'CLIENTE_TIENDA';
  }

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

  buscarPorId() {
    if (!this.idSearchText || this.idSearchText.trim() === '') {
      this.cargarProductos();
      return;
    }
    const id = Number(this.idSearchText);
    if (isNaN(id)) return;
    this.loading = true;
    this.errorMessage = '';
    this.productoService.buscarPorId(id).subscribe({
      next: (producto) => {
        this.productos = [producto];
        this.loading = false;
      },
      error: () => {
        this.errorMessage = `Producto con ID ${id} no encontrado.`;
        this.loading = false;
      }
    });
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

  onProductoSeleccionado(producto: Producto) {
    this.selectedProducto = producto;
  }

  agregarAlCarrito(producto: Producto) {
    this.cartService.addItem(producto.id, producto.nombre, producto.precio, 1);
  }
}
