import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../services/producto';
import { Producto } from '../../../../models/producto.model';
import { FilterProductosPipe } from '../../../../pipes/filter.pipe';

@Component({
  selector: 'app-producto-list',
  standalone: true, // Define que este componente no necesita de un NgModule tradicional
  imports: [CommonModule, RouterLink, FormsModule, FilterProductosPipe], // Módulos y pipes necesarios en este componente
  templateUrl: './producto-list.html',
  styleUrl: './producto-list.scss',
})
export class ProductoList implements OnInit {
  // Inyectar el servicio encargado de la comunicación con el backend
  private productoService = inject(ProductoService);

  productos: Producto[] = [];
  searchText: string = ''; // Variable enlazada al input de búsqueda para el pipe filterProductos
  mostrarBajoStock: boolean = false; // Estado del checkbox de bajo stock

  // Ciclo de vida: se ejecuta al inicializar el componente
  ngOnInit(): void {
    this.cargarProductos();
  }

  // Llama al servicio para obtener todos los productos y los guarda en el array 'productos'
  cargarProductos() {
    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos = data;
      },
      error: (err) => console.error('Error al cargar productos', err),
    });
  }

  // Método que se activa al cambiar el checkbox de bajo stock
  filtrarBajoStock() {
    if (this.mostrarBajoStock) {
      // Pide al backend solo los productos cuyo stock sea <= 10
      this.productoService.bajoStock(10).subscribe({
        next: (data) => (this.productos = data),
        error: (err) => console.error('Error al cargar bajo stock', err),
      });
    } else {
      // Vuelve a cargar todos los productos
      this.cargarProductos();
    }
  }

  // Método para eliminar un producto luego de confirmación del usuario
  eliminarProducto(id: number) {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.productoService.eliminar(id).subscribe({
        next: () => {
          // Recarga la lista tras eliminar exitosamente
          this.cargarProductos();
        },
        error: (err) => console.error('Error al eliminar', err),
      });
    }
  }
}
