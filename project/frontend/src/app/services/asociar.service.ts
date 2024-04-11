import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Centro } from '../centro';
import { Gerente } from '../gerente';
import { BACKEND_URI } from '../config/config';

@Injectable({
  providedIn: 'root'
})
export class AsociacionesService {

  constructor(private httpClient: HttpClient) { }

  asociarCentroGerente(idCentro: number, idGerente: number): void {
    this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + idCentro + '/gerente',
      {
        "idGerente": idGerente
      }
    ) 
  }

  //Para el Backend
  /*eliminarAsociacionCentro(idCentro: number): void {
    this.httpClient.delete<void>(BACKEND_URI + '/centro/' + idCentro + '/gerente?gerente=' + idGerente).subscribe()
      
  }*/

  obtenerGerenteAsociado(idCentro: number): Observable<Gerente> {
    return this.httpClient.get<Gerente>(BACKEND_URI + '/centros/' + idCentro + '/gerente')
      
  }

  obtenerCentrosAsociados(idGerente: number): Observable<Centro[]> {
    return this.httpClient.get<Centro[]>(BACKEND_URI + '/centros/'+ idGerente + '/gerente')
      
  }

  
}
