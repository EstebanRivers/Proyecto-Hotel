export interface ReservaRequest {
    idHuesped: number,
    idHabitacion: number,
    fechaEntrada: string,
    fechaSalida: string,
    idEstadoReserva: number
}

export interface ReservaResponse {
    id: number,
    huesped: DatoHuesped,
    habitacion: DatoHabitacion,
    fechaEntrada: string,
    fechaSalida: string,
    estadoReserva: string
}

export interface DatoHuesped {
    id: number,
    nombre: string,
    edad: number,
    email: string,
    telefono: string,
    nacionalidad: string,
    documento: string
}

export interface DatoHabitacion {
    id: number,
    numero: string,
    tipo: string,
    precio: number,
    capacidad: number
}