export interface SolicitudAdopcion {
  id: number;
  nombreSolicitante: string;
  telefono: string;
  motivo: string;
  estado: string;
  mascotaId: number;
  nombreMascota: string;
}

export interface SolicitudAdopcionRequest {
  mascotaId: number;
  nombreSolicitante: string;
  telefono: string;
  motivo: string;
}
