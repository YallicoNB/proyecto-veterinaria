export enum TipoMascota {
  PERRO = 'PERRO',
  GATO = 'GATO',
  OTRO = 'OTRO'
}

export enum Sexo {
  MACHO = 'MACHO',
  HEMBRA = 'HEMBRA'
}

export enum EstadoMascota {
  ACTIVA = 'ACTIVA',
  ADOPTADA = 'ADOPTADA',
  FALLECIDA = 'FALLECIDA'
}

export interface Mascota {
  id: number;
  nombre: string;
  tipo: TipoMascota;
  raza: string;
  edad: number;
  sexo: Sexo;
  color: string;
  peso: number;
  observaciones: string;
  fotoUrl: string;
  estado: EstadoMascota;
  fechaRegistro: string;
}
