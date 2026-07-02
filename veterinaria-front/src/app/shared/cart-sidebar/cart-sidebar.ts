import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService } from '../../core/services/cart';

@Component({
  selector: 'app-cart-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart-sidebar.html',
  styleUrl: './cart-sidebar.scss',
})
export class CartSidebar {
  private cartService = inject(CartService);
  private router = inject(Router);

  open = signal(false);
  items = this.cartService.items;
  total = this.cartService.total;
  itemCount = this.cartService.itemCount;

  toggle() {
    this.open.update(v => !v);
  }

  close() {
    this.open.set(false);
  }

  remove(productoId: number) {
    this.cartService.removeItem(productoId);
  }

  updateQty(productoId: number, cantidad: number) {
    this.cartService.updateQuantity(productoId, cantidad);
  }

  checkout() {
    this.close();
    this.router.navigate(['/tienda/ventas/nueva'], {
      state: { fromCart: true }
    });
  }
}
