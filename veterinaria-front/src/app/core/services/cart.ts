import { Injectable, signal, computed, effect } from '@angular/core';

export interface CartItem {
  productoId: number;
  nombre: string;
  precio: number;
  cantidad: number;
}

const STORAGE_KEY = 'veterinaria_cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private itemsSignal = signal<CartItem[]>(this.loadFromStorage());

  items = this.itemsSignal.asReadonly();

  total = computed(() =>
    this.itemsSignal().reduce((sum, item) => sum + item.precio * item.cantidad, 0)
  );

  itemCount = computed(() =>
    this.itemsSignal().reduce((sum, item) => sum + item.cantidad, 0)
  );

  constructor() {
    effect(() => {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.itemsSignal()));
    });
  }

  addItem(productoId: number, nombre: string, precio: number, cantidad: number = 1) {
    this.itemsSignal.update(items => {
      const existing = items.find(i => i.productoId === productoId);
      if (existing) {
        return items.map(i =>
          i.productoId === productoId ? { ...i, cantidad: i.cantidad + cantidad } : i
        );
      }
      return [...items, { productoId, nombre, precio, cantidad }];
    });
  }

  removeItem(productoId: number) {
    this.itemsSignal.update(items => items.filter(i => i.productoId !== productoId));
  }

  updateQuantity(productoId: number, cantidad: number) {
    if (cantidad < 1) return;
    this.itemsSignal.update(items =>
      items.map(i => (i.productoId === productoId ? { ...i, cantidad } : i))
    );
  }

  clear() {
    this.itemsSignal.set([]);
  }

  private loadFromStorage(): CartItem[] {
    try {
      const data = localStorage.getItem(STORAGE_KEY);
      return data ? JSON.parse(data) : [];
    } catch {
      return [];
    }
  }
}
