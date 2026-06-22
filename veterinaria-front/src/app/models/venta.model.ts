export interface DetalleVenta {
  id: number;
  productoId: number;
  nombreProducto: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface Venta {
  id: number;
  fecha: string;
  total: number;
  detalles: DetalleVenta[];
}

export interface DetalleVentaRequest {
  productoId: number;
  cantidad: number;
}

export interface VentaRequest {
  detalles: DetalleVentaRequest[];
}
