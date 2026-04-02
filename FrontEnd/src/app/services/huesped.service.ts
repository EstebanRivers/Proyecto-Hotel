import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { catchError, map, Observable, of, throwError } from "rxjs";
import { HuespedRequest, HuespedResponse } from "../models/Huesped.model";

@Injectable({
  providedIn: 'root'
})

export class HuespedesService {

  private apiUrl: string = environment.apiUrl.concat('huespedes')

  constructor(private http: HttpClient) { }

  getHuespedes(): Observable<HuespedResponse[]> {
    return this.http.get<HuespedResponse[]>(this.apiUrl).pipe(
      map(huespedes => huespedes.sort()),
      catchError(error => {
        console.error('Error al obtener los huéspedes', error);
        return of([]);
      })
    );
  }

  postHuesped(huesped: HuespedRequest): Observable<HuespedResponse> {
    return this.http.post<HuespedResponse>(this.apiUrl, huesped).pipe(
      catchError(error => {
        console.error('Error al registrar el huesped', error);
        return throwError(() => error);
      })
    );
  }

  putHuesped(huesped: HuespedRequest, huespedId: number): Observable<HuespedResponse>{
    return this.http.put<HuespedResponse>(`${this.apiUrl}/${huespedId}`, huesped).pipe(
      catchError(error => {
        console.error('Error al actualizar un huésped', error);
        throw error;
      })
    )
  }

  deleteHuesped(huespedId: number): Observable<HuespedResponse> {
    return this.http.delete<HuespedResponse>(`${this.apiUrl}/${huespedId}`).pipe(
      catchError(error => {
        console.error('Error al eliminar el huesped', error);
        return throwError(() => error);
      })
    );
  }

}