export interface HabitacionRequest {
    numero: number,
    tipo: string,
    precio: number,
    capacidad: number,
    idEstadoHabitacion: number
}

export interface HabitacionResponse {
    id: number,        
    numero: number,
    tipo: string,
    precio: number, 
    capacidad: number,
    estadoHabitacion: string
}