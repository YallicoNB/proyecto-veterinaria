import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { VentaService } from '../services/venta';
import { ProductoService } from '../../productos/services/producto';
import { Producto } from '../../../../models/producto.model';
import { CartService } from '../../../../core/services/cart';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-venta-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './venta-form.html',
  styleUrl: './venta-form.scss',
})
export class VentaForm implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private ventaService = inject(VentaService);
  private productoService = inject(ProductoService);
  private router = inject(Router);
  private cartService = inject(CartService);

  ventaForm!: FormGroup;
  productos: Producto[] = [];
  private subRefs: Subscription[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;

  ngOnInit(): void {
    this.ventaForm = this.fb.group({
      detalles: this.fb.array([])
    });
    this.loading = true;

    const navigation = this.router.getCurrentNavigation();
    const fromCart = navigation?.extras?.state?.['fromCart'];

    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos = data;
        this.loading = false;
        if (fromCart && this.cartService.items().length > 0) {
          this.cargarDesdeCarrito();
        } else {
          this.agregarDetalle();
        }
      },
      error: () => {
        this.errorMessage = 'Error al cargar productos.';
        this.loading = false;
      }
    });
  }

  private cargarDesdeCarrito() {
    for (const item of this.cartService.items()) {
      const producto = this.productos.find(p => p.id === item.productoId);
      if (producto) {
        this.agregarDetalle(producto.id, item.cantidad);
      }
    }
  }

  ngOnDestroy(): void {
    this.subRefs.forEach(s => s.unsubscribe());
  }

  get detalles(): FormArray {
    return this.ventaForm.get('detalles') as FormArray;
  }

  agregarDetalle(productoId?: number, cantidad?: number) {
    const detalleForm = this.fb.group({
      productoId: [productoId ?? null, Validators.required],
      cantidad: [cantidad ?? 1, [Validators.required, Validators.min(1)]],
      precioUnitario: [{ value: 0, disabled: true }],
      subtotal: [{ value: 0, disabled: true }]
    });

    const sub = new Subscription();
    sub.add(
      detalleForm.get('productoId')!.valueChanges.subscribe(id => {
        const p = this.productos.find(prod => prod.id == id);
        if (p) {
          detalleForm.patchValue({ precioUnitario: p.precio });
          this.calcularSubtotal(detalleForm);
        }
      })
    );
    sub.add(
      detalleForm.get('cantidad')!.valueChanges.subscribe(() => {
        this.calcularSubtotal(detalleForm);
      })
    );
    this.subRefs.push(sub);
    this.detalles.push(detalleForm);
  }

  eliminarDetalle(index: number) {
    this.subRefs[index]?.unsubscribe();
    this.subRefs.splice(index, 1);
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

    this.errorMessage = '';
    this.successMessage = '';
    this.loading = true;
    const requestData = {
      detalles: this.detalles.value.map((d: any) => ({
        productoId: d.productoId,
        cantidad: d.cantidad
      }))
    };

    this.ventaService.registrar(requestData).subscribe({
      next: () => {
        this.successMessage = 'Venta registrada correctamente';
        this.loading = false;
        this.subRefs.forEach(s => s.unsubscribe());
        this.subRefs = [];
        this.ventaForm.reset();
        this.detalles.clear();
        this.agregarDetalle();
        this.cartService.clear();
        setTimeout(() => this.router.navigate(['/tienda/ventas']), 2000);
      },
      error: () => {
        this.errorMessage = 'Error al registrar la venta.';
        this.loading = false;
      }
    });
  }
}
