export interface HistoriaClinica {
  id: number;
  mascotaId: number;
  nombreMascota: string;
  motivoConsulta: string;
  diagnostico: string;
  tratamiento: string;
  fechaCreacion: string;
}

export interface HistoriaClinicaRequest {
  mascotaId: number;
  motivoConsulta: string;
  diagnostico?: string;
  tratamiento?: string;
}
