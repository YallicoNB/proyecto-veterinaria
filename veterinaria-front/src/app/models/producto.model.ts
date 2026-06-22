export interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  categoria: string;
  fechaRegistro: string;
}

export interface ProductoRequest {
  nombre: string;
  precio: number;
  stock: number;
  categoria?: string;
}
