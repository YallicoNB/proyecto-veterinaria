export enum EstadoConsulta {
  PENDIENTE = 'PENDIENTE',
  REALIZADA = 'REALIZADA',
  CANCELADA = 'CANCELADA'
}

export interface Consulta {
  id: number;
  mascotaId: number;
  nombreMascota: string;
  veterinarioId: number;
  nombreVeterinario: string;
  sintomas: string;
  diagnostico: string;
  receta: string;
  estado: EstadoConsulta;
  fecha: string;
}

export interface ConsultaRequest {
  mascotaId: number;
  veterinarioId?: number;
  sintomas: string;
}

export interface AtenderConsultaRequest {
  diagnostico?: string;
  receta?: string;
}
