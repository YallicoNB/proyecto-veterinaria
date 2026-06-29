import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductoService } from '../services/producto';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './producto-form.html',
  styleUrl: './producto-form.scss',
})
export class ProductoForm implements OnInit {
  private fb = inject(FormBuilder);
  private productoService = inject(ProductoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  productoForm!: FormGroup;
  productoId: number | null = null;
  isEditMode: boolean = false;
  errorMessage: string = '';
  loading: boolean = false;

  ngOnInit(): void {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      precio: [0, [Validators.required, Validators.min(0.1)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      categoria: ['']
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.productoId = Number(idParam);
      this.isEditMode = true;
      this.cargarProducto(this.productoId);
    }
  }

  cargarProducto(id: number) {
    this.loading = true;
    this.productoService.buscarPorId(id).subscribe({
      next: (producto) => {
        this.productoForm.patchValue({
          nombre: producto.nombre,
          precio: producto.precio,
          stock: producto.stock,
          categoria: producto.categoria
        });
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Error al cargar producto.';
        this.loading = false;
      }
    });
  }

  guardar() {
    if (this.productoForm.invalid) {
      this.productoForm.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.loading = true;
    const data = this.productoForm.value;

    if (this.isEditMode && this.productoId) {
      this.productoService.actualizar(this.productoId, data).subscribe({
        next: () => this.router.navigate(['/tienda/productos']),
        error: () => {
          this.errorMessage = 'Error al actualizar producto.';
          this.loading = false;
        }
      });
    } else {
      this.productoService.crear(data).subscribe({
        next: () => this.router.navigate(['/tienda/productos']),
        error: () => {
          this.errorMessage = 'Error al crear producto.';
          this.loading = false;
        }
      });
    }
  }
}
