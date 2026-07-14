import { Pipe, PipeTransform } from '@angular/core';
import { Producto } from '../models/producto.model';

// Este decorador define el nombre con el que se usará el pipe en las plantillas HTML
@Pipe({
  name: 'filterProductos',
  standalone: true, // Pipe independiente que no requiere declaración en un NgModule
})
export class FilterProductosPipe implements PipeTransform {
  // Método principal del pipe. Toma el array de productos y el texto de búsqueda.
  transform(productos: Producto[], searchText: string): Producto[] {
    // Si no hay productos, retorna un array vacío
    if (!productos) return [];

    // Si no se escribió nada en la búsqueda, retorna la lista completa sin cambios
    if (!searchText) return productos;

    // Convertir el texto de búsqueda a minúsculas para ignorar mayúsculas/minúsculas
    const lowerText = searchText.toLowerCase();

    // Filtra y devuelve solo los productos cuyo nombre incluya el texto ingresado
    return productos.filter((producto) => {
      return producto.nombre.toLowerCase().includes(lowerText);
    });
  }
}
