import { Injectable } from "@angular/core";
import { Observable, map, of } from "rxjs";
import { Gerente } from "./gerente";
import { HttpClient, HttpParams } from "@angular/common/http";
import { BACKEND_URI } from "./config/config";
import { JwtResponse } from "./entities/login";

@Injectable({
  providedIn: 'root'
})
export class GerentesService {
  private gerentes: Gerente [] = [
    {idUsuario: 1, empresa: 'Juan SL', id: 123},
    {idUsuario: 2, empresa: 'Paco SL', id: 456},
    {idUsuario: 3, empresa: 'Marta SL', id: 678}
  ];

  constructor(private httpClient: HttpClient) { }

  getGerentes(): Observable<Gerente []> {
    //return this.gerentes;
    return this.httpClient.get<Gerente[]>(BACKEND_URI + '/gerente');
  }

  addGerente(gerente: Gerente): Observable<Gerente> {
    //gerente.idUsuario = this.gerentes.length + 1;
    //this.gerentes.push(gerente);
    return this.httpClient.post<Gerente>(BACKEND_URI + '/gerente', 
    {
      "idUsuario": gerente.idUsuario,
      "empresa": gerente.empresa
    });
    /*
    .pipe(
      catchError(error => {
        if (error.status === 404) {
          // Aquí se maneja el error 404
          console.error('Recurso no encontrado:', error);
        };
    */
  }

  editarGerente(gerente: Gerente) {
    //let indice = this.gerentes.findIndex(c => c.idUsuario == gerente.idUsuario);
    //this.gerentes[indice] = gerente;
    return this.httpClient.put<Gerente>(BACKEND_URI + '/gerente/' + gerente.id, gerente);
  }

  eliminargGerente(id: number) {
    //let indice = this.gerentes.findIndex(c => c.idUsuario == id);
    //this.gerentes.splice(indice, 1);
    //this.actualizarIdUsuarios();
    return this.httpClient.delete<void>(BACKEND_URI + '/gerente/' + id);
  }

  getGerente(id: number): Observable<Gerente> {
    return this.httpClient.get<Gerente>(BACKEND_URI + '/gerente/' + id);
  }
}
