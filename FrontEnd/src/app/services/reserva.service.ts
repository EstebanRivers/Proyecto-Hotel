import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { ReservaRequest, ReservaResponse } from "../models/Reserva.model";
import { catchError, map, Observable, of, throwError } from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class ReservasService {

  private apiUrl: string = environment.apiUrl.concat('reservas')

  constructor(private http: HttpClient) { }

  getReservas(): Observable<ReservaResponse[]> {
    return this.http.get<ReservaResponse[]>(this.apiUrl).pipe(
      map(reservas => reservas.sort()),
      catchError(error => {
        console.error('Error al obtener los reservas', error);
        return of([]);
      })
    );
  }

  postReserva(reserva: ReservaRequest): Observable<ReservaResponse> {
    return this.http.post<ReservaResponse>(this.apiUrl, reserva).pipe(
      catchError(error => {
        console.error('Error al registrar el reserva', error);
        return throwError(() => error);
      })
    );
  }

  putReserva(reserva: ReservaRequest, reservaId: number): Observable<ReservaResponse>{
    return this.http.put<ReservaResponse>(`${this.apiUrl}/${reservaId}`, reserva).pipe(
      catchError(error => {
        console.error('Error al actualizar un reserva', error);
        throw error;
      })
    )
  }

  deleteReserva(reservaId: number): Observable<ReservaResponse> {
    return this.http.delete<ReservaResponse>(`${this.apiUrl}/${reservaId}`).pipe(
      catchError(error => {
        console.error('Error al eliminar el reserva', error);
        return throwError(() => error);
      })
    );
  }

}