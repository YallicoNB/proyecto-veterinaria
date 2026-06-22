export enum Rol {
  ADMIN = 'ADMIN',
  VETERINARIO = 'VETERINARIO',
  EMPLEADO_LAVANDERIA = 'EMPLEADO_LAVANDERIA',
  ADOPTANTE = 'ADOPTANTE',
  CLIENTE_TIENDA = 'CLIENTE_TIENDA'
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  rol: Rol;
  activo: boolean;
  fechaCreacion: string;
}

export interface UsuarioRequest {
  username: string;
  password: string;
  email: string;
  rol: Rol;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  rol: string;
}
