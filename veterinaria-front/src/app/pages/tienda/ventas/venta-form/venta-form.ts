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
    this.ventaForm = this.fb.group({
      detalles: this.fb.array([], Validators.required)
    });
  }

  ngOnInit(): void {
    this.productoService.listar().subscribe({
      next: (data) => this.productos = data,
      error: (err) => console.error('Error cargando productos', err)
    });
    this.agregarDetalle();
  }

  get detalles(): FormArray {
    return this.ventaForm.get('detalles') as FormArray;
  }

  agregarDetalle() {
    const detalleForm = this.fb.group({
      productoId: [null, Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precioUnitario: [{ value: 0, disabled: true }],
      subtotal: [{ value: 0, disabled: true }]
    });

    // Listen to changes in productoId to update precioUnitario
    detalleForm.get('productoId')?.valueChanges.subscribe(id => {
      const p = this.productos.find(prod => prod.id == id);
      if (p) {
        detalleForm.patchValue({ precioUnitario: p.precio });
        this.calcularSubtotal(detalleForm);
      }
    });

    // Listen to changes in cantidad to update subtotal
    detalleForm.get('cantidad')?.valueChanges.subscribe(() => {
      this.calcularSubtotal(detalleForm);
    });

    this.detalles.push(detalleForm);
  }

  eliminarDetalle(index: number) {
    this.detalles.removeAt(index);
  }

  calcularSubtotal(detalleForm: FormGroup) {
    const cantidad = detalleForm.get('cantidad')?.value || 0;
    const precio = detalleForm.get('precioUnitario')?.value || 0;
    detalleForm.patchValue({ subtotal: cantidad * precio });
  }

  get totalVenta(): number {
    return this.detalles.controls.reduce((acc, current) => {
      return acc + (current.get('subtotal')?.value || 0);
    }, 0);
  }

  guardar() {
    if (this.ventaForm.invalid || this.detalles.length === 0) {
      this.ventaForm.markAllAsTouched();
      return;
    }

    // Prepare data for the request
    const requestData = {
      detalles: this.detalles.value.map((d: any) => ({
        productoId: d.productoId,
        cantidad: d.cantidad
      }))
    };

    this.ventaService.registrar(requestData).subscribe({
      next: () => this.router.navigate(['/tienda/ventas']),
      error: (err) => console.error('Error registrando venta', err)
    });
  }
}
