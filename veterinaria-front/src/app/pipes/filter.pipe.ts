import { Pipe, PipeTransform } from '@angular/core';
import { Producto } from '../models/producto.model';

@Pipe({
  name: 'filterProductos',
  standalone: true
})
export class FilterProductosPipe implements PipeTransform {
  transform(productos: Producto[], searchText: string): Producto[] {
    if (!productos) return [];
    if (!searchText) return productos;

    const lowerText = searchText.toLowerCase();
    return productos.filter(producto => {
      return producto.nombre.toLowerCase().includes(lowerText);
    });
  }
}
