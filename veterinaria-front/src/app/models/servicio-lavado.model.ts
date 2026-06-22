export enum TipoServicio {
  BAÑO = 'BAÑO',
  CORTE = 'CORTE',
  SPA = 'SPA',
  LIMPIEZA = 'LIMPIEZA',
  PELUQUERIA = 'PELUQUERIA'
}

export interface ServicioLavado {
  id: number;
  mascotaId: number;
  nombreMascota: string;
  tipoServicio: TipoServicio;
  precio: number;
  fechaHora: string;
  estado: string;
  observaciones: string;
}

export interface ServicioLavadoRequest {
  mascotaId: number;
  tipoServicio: TipoServicio;
  precio: number;
  fechaHora: string;
  observaciones?: string;
}
