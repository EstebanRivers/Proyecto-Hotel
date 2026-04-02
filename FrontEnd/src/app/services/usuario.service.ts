import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { UsuarioRequest, UsuarioResponse } from "../models/Usuario.model";
import { catchError, map, Observable, of, throwError } from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class UsuariosService {

  private apiUrl: string = environment.apiUsuarios;

  constructor(private http: HttpClient) { }

  getUsuarios(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.apiUrl).pipe(
      map(usuarios => usuarios.sort()),
      catchError(error => {
        console.error('Error al obtener los usuarios', error);
        return of([]);
      })
    );
  }

  postUsuario(usuario: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(this.apiUrl, usuario).pipe(
      catchError(error => {
        console.error('Error al registrar el usuario', error);
        return throwError(() => error);
      })
    );
  }

  putUsuario(usuario: UsuarioRequest, usuarioId: number): Observable<UsuarioResponse>{
    return this.http.put<UsuarioResponse>(`${this.apiUrl}/${usuarioId}`, usuario).pipe(
      catchError(error => {
        console.error('Error al actualizar un usuario', error);
        throw error;
      })
    )
  }

  deleteUsuario(usuarioId: number): Observable<UsuarioResponse> {
    return this.http.delete<UsuarioResponse>(`${this.apiUrl}/${usuarioId}`).pipe(
      catchError(error => {
        console.error('Error al eliminar el usuario', error);
        return throwError(() => error);
      })
    );
  }

}