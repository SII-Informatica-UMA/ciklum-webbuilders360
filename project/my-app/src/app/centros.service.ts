import { Injectable } from '@angular/core';
import { Centro } from './centro';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BACKEND_URI } from './config/config';
import { Gerente } from './gerente';


@Injectable({
  providedIn: 'root'
})
export class CentrosService {
  private centros: Centro [] = [
    {idCentro: 1, nombre: 'JuanGym', direccion: 'c/calle 123'},
    {idCentro: 2, nombre: 'AnaGimnasio', direccion: 'c/calle 321'},
    {idCentro: 3, nombre: 'PacoGym', direccion: 'c/calle 4'}
  ];
  // CON EL BACKEND
  constructor(private httpClient: HttpClient) { }

  getCentros(gerente?: number): Observable<Centro[]> {
    let params = new HttpParams();
    if (gerente != undefined) {
      params = params.append('gerente', (gerente ?? '').toString()); 
    }
    return this.httpClient.get<Centro[]>(BACKEND_URI + '/centro', { params });
  }


  addCentro(centro: Centro): void {
    //centro.idCentro = this.centros.length + 1;
    //this.centros.push(centro);
    this.httpClient.post<Centro>(BACKEND_URI + '/centro', 
    {
      "nombre": centro.nombre,
      "direccion": centro.direccion
    }).subscribe();
  }

  editarCentro(centro: Centro): void {
    //let indice = this.centros.findIndex(c => c.idCentro == centro.idCentro);
    //this.centros[indice] = centro;
    this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro.idCentro, centro).subscribe();
  }

  eliminarcCentro(id: number) {
    //let indice = this.centros.findIndex(c => c.idCentro == id);
    //this.centros.splice(indice, 1);
    this.httpClient.delete<void>(BACKEND_URI + '/centro/' + id).subscribe();
  }

  getCentro(idCentro: number): Observable<Centro> {
    return this.httpClient.get<Centro>(BACKEND_URI + '/centro/' + idCentro);
  }

  asociarCentroGerente(idCentro: number, idGerente: number): Observable<Centro> {
    return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + idCentro + '/gerente',
      {
        "idGerente": idGerente
      }
    )
  }

  getGerenteAsociado(idCentro: number): Observable<Gerente | undefined> {
    return this.httpClient.get<Gerente>(BACKEND_URI + '/centros/' + idCentro + '/gerente')
  }

  quitarAsociacionCentroGerente(idCentro: number, idGerente: number): void {
    this.httpClient.delete<void>(BACKEND_URI + '/centro/' + idCentro + '/gerente?gerente=' + idGerente).subscribe()
  }

  /*
  constructor() { }

  getCentros(): Centro [] {
    return this.centros;
  }

  addCentro(centro: Centro) {
    centro.idCentro = this.centros.length + 1;
    this.centros.push(centro);
  }

  editarCentro(centro: Centro) {
    let indice = this.centros.findIndex(c => c.idCentro == centro.idCentro);
    this.centros[indice] = centro;
  }

  eliminarcCentro(id: number) {
    let indice = this.centros.findIndex(c => c.idCentro == id);
    this.centros.splice(indice, 1);
  }
  */
}
