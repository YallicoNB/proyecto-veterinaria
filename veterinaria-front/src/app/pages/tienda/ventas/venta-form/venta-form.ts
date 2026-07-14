import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { VentaService } from '../services/venta';
import { ProductoService } from '../../productos/services/producto';
import { Producto } from '../../../../models/producto.model';

@Component({
  selector: 'app-venta-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './venta-form.html',
  styleUrl: './venta-form.scss',
})
export class VentaForm implements OnInit {
  private fb = inject(FormBuilder);
  private ventaService = inject(VentaService);
  private productoService = inject(ProductoService);
  private router = inject(Router);

  ventaForm: FormGroup;
  productos: Producto[] = [];

  constructor() {
    // Inicializa el formulario de la venta con un arreglo vacío de 'detalles'
    // que es requerido (no se puede vender nada sin detalles).
    this.ventaForm = this.fb.group({
      detalles: this.fb.array([], Validators.required),
    });
  }

  ngOnInit(): void {
    // Carga los productos disponibles para llenar el <select> en la vista
    this.productoService.listar().subscribe({
      next: (data) => (this.productos = data),
      error: (err) => console.error('Error cargando productos', err),
    });
    // Agrega automáticamente una primera fila en blanco al cargar la página
    this.agregarDetalle();
  }

  // Getter (accesador) útil para obtener el FormArray rápidamente en el código y HTML
  get detalles(): FormArray {
    return this.ventaForm.get('detalles') as FormArray;
  }

  // Crea y añade un nuevo FormGroup (una nueva fila) al FormArray de detalles
  agregarDetalle() {
    const detalleForm = this.fb.group({
      productoId: [null, Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precioUnitario: [{ value: 0, disabled: true }], // deshabilitado para que el usuario no modifique el precio base
      subtotal: [{ value: 0, disabled: true }],
    });

    // Escucha cada vez que cambia el producto seleccionado en esa fila
    detalleForm.get('productoId')?.valueChanges.subscribe((id) => {
      // Busca el producto en el array local 'productos' para extraer su precio
      const p = this.productos.find((prod) => prod.id == id);
      if (p) {
        // Actualiza el campo precioUnitario de la fila
        detalleForm.patchValue({ precioUnitario: p.precio });
        // Recalcula el subtotal en base a la cantidad actual
        this.calcularSubtotal(detalleForm);
      }
    });

    // Escucha los cambios manuales que el usuario haga en la cantidad
    detalleForm.get('cantidad')?.valueChanges.subscribe(() => {
      this.calcularSubtotal(detalleForm);
    });

    // Ingresa el nuevo grupo al arreglo
    this.detalles.push(detalleForm);
  }

  // Remueve una fila en una posición (index) específica
  eliminarDetalle(index: number) {
    this.detalles.removeAt(index);
  }

  // Calcula Cantidad x Precio Unitario para una fila
  calcularSubtotal(detalleForm: FormGroup) {
    const cantidad = detalleForm.get('cantidad')?.value || 0;
    const precio = detalleForm.get('precioUnitario')?.value || 0;
    detalleForm.patchValue({ subtotal: cantidad * precio });
  }

  // Calcula el gran total sumando el subtotal de todas las filas activas en el FormArray
  get totalVenta(): number {
    return this.detalles.controls.reduce((acc, current) => {
      return acc + (current.get('subtotal')?.value || 0);
    }, 0);
  }

  // Método que procesa el envío de la venta completa hacia el backend
  guardar() {
    // Si hay errores de validación o no hay ningún producto, se detiene
    if (this.ventaForm.invalid || this.detalles.length === 0) {
      this.ventaForm.markAllAsTouched();
      return;
    }

    // Se transforma la data del FormArray a la estructura que el backend espera (VentaRequest)
    const requestData = {
      detalles: this.detalles.value.map((d: any) => ({
        productoId: d.productoId,
        cantidad: d.cantidad,
      })),
    };

    // Envío (POST)
    this.ventaService.registrar(requestData).subscribe({
      next: () => this.router.navigate(['/tienda/ventas']), // Redirige a la tabla si es exitoso
      error: (err) => console.error('Error registrando venta', err),
    });
  }
}
