import { Rol } from "../constants/Rol"

export interface UsuarioRequest {
    username: string,
    password: string,
    rol: Rol
}

export interface UsuarioResponse {
    id: number,
    username: string,
    rol: Rol
}