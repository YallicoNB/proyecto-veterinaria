export interface SolicitudAdopcion {
  id: number;
  nombreSolicitante: string;
  telefono: string;
  motivo: string;
  estado: string;
  mascotaId: number;
  nombreMascota: string;
  usuarioId: number;
}

export interface SolicitudAdopcionRequest {
  mascotaId: number;
  nombreSolicitante: string;
  telefono: string;
  motivo: string;
}
