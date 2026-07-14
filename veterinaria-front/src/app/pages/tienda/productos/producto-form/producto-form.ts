import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductoService } from '../services/producto';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], // ReactiveFormsModule permite crear formularios reactivos
  templateUrl: './producto-form.html',
  styleUrl: './producto-form.scss',
})
export class ProductoForm implements OnInit {
  private fb = inject(FormBuilder);
  private productoService = inject(ProductoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute); // Para obtener parámetros de la URL actual

  productoForm: FormGroup;
  productoId: number | null = null;
  isEditMode: boolean = false; // Bandera para saber si el formulario creará o editará

  constructor() {
    // Definición de la estructura y reglas de validación del formulario
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      precio: [0, [Validators.required, Validators.min(0.1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      categoria: [''],
    });
  }

  ngOnInit(): void {
    // Extrae el parámetro ':id' de la ruta (ej: /tienda/productos/5/editar)
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.productoId = Number(idParam);
      this.isEditMode = true; // Activa el modo edición
      this.cargarProducto(this.productoId);
    }
  }

  // Si estamos editando, solicitamos los datos actuales al backend y los inyectamos en el formulario
  cargarProducto(id: number) {
    this.productoService.buscarPorId(id).subscribe({
      next: (producto) => {
        // patchValue llena los campos del formulario con los valores recibidos
        this.productoForm.patchValue({
          nombre: producto.nombre,
          precio: producto.precio,
          stock: producto.stock,
          categoria: producto.categoria,
        });
      },
      error: (err) => console.error('Error al cargar producto', err),
    });
  }

  // Método que se ejecuta al enviar el formulario (ngSubmit)
  guardar() {
    // Si el formulario no cumple las reglas (Validators), marcamos todo como 'tocado' para mostrar errores en HTML
    if (this.productoForm.invalid) {
      this.productoForm.markAllAsTouched();
      return;
    }

    const data = this.productoForm.value; // Extrae un objeto con los valores ingresados

    if (this.isEditMode && this.productoId) {
      // Actualización (PUT)
      this.productoService.actualizar(this.productoId, data).subscribe({
        next: () => this.router.navigate(['/tienda/productos']), // Redirige a la lista al terminar
        error: (err) => console.error('Error al actualizar', err),
      });
    } else {
      // Creación (POST)
      this.productoService.crear(data).subscribe({
        next: () => this.router.navigate(['/tienda/productos']),
        error: (err) => console.error('Error al crear', err),
      });
    }
  }
}
