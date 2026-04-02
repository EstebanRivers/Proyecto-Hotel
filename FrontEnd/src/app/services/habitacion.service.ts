import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { catchError, map, Observable, of, throwError } from "rxjs";
import { HabitacionRequest, HabitacionResponse } from "../models/Habitacion.model";

@Injectable({
  providedIn: 'root'
})

export class HabitacionesSerivice {

  private apiUrl: string = environment.apiUrl.concat('habitaciones')

  constructor(private http: HttpClient) { }

  getHabitaciones(): Observable<HabitacionResponse[]> {
    return this.http.get<HabitacionResponse[]>(this.apiUrl).pipe(
      map(habitaciones => habitaciones.sort()),
      catchError(error => {
        console.error('Error al obtener los habitación', error);
        return of([]);
      })
    );
  }

  postHabitacion(habitacion: HabitacionRequest): Observable<HabitacionResponse> {
    return this.http.post<HabitacionResponse>(this.apiUrl, habitacion).pipe(
      catchError(error => {
        console.error('Error al registrar el habitación', error);
        return throwError(() => error);
      })
    );
  }

  putHabitacion(habitacion: HabitacionRequest, habitacionId: number): Observable<HabitacionResponse>{
    return this.http.put<HabitacionResponse>(`${this.apiUrl}/${habitacionId}`, habitacion).pipe(
      catchError(error => {
        console.error('Error al actualizar un habitación', error);
        throw error;
      })
    )
  }

  deleteHabitacion(habitacionId: number): Observable<HabitacionResponse> {
    return this.http.delete<HabitacionResponse>(`${this.apiUrl}/${habitacionId}`).pipe(
      catchError(error => {
        console.error('Error al eliminar el habitación', error);
        return throwError(() => error);
      })
    );
  }
}