export interface Vacuna {
  id: number;
  mascotaId: number;
  nombreMascota: string;
  nombreVacuna: string;
  fechaAplicacion: string;
  fechaProxima: string;
  lote: string;
}

export interface VacunaRequest {
  mascotaId: number;
  nombreVacuna: string;
  fechaAplicacion?: string;
  fechaProxima?: string;
  lote?: string;
}
