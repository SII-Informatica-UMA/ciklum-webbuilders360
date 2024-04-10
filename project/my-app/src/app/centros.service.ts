import { Injectable } from '@angular/core';
import { Centro } from './centro';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BACKEND_URI } from './config/config';


@Injectable({
  providedIn: 'root'
})
export class CentrosService {
  private centros: Centro [] = [
    {idCentro: 1, nombre: 'JuanGym', direccion: 'c/calle 123'},
    {idCentro: 2, nombre: 'AnaGimnasio', direccion: 'c/calle 321'},
    {idCentro: 3, nombre: 'PacoGym', direccion: 'c/calle 4'}
  ];
  /* CON EL BACKEND
  constructor(private httpClient: HttpClient) { }

  getCentros(gerente?: number): Observable<Centro[]> {
    let params = new HttpParams();
    if (gerente != undefined) {
      params = params.append('gerente', (gerente ?? '').toString()); 
    }
    return this.httpClient.get<Centro[]>(BACKEND_URI + '/centro', { params });
  }


  addCentro(centro: Centro): Observable<Centro> {
    //centro.idCentro = this.centros.length + 1;
    //this.centros.push(centro);
    return this.httpClient.post<Centro>(BACKEND_URI + '/centro', 
    {
      "nombre": centro.nombre,
      "direccion": centro.direccion
    });
  }

  editarCentro(centro: Centro): Observable<Centro> {
    //let indice = this.centros.findIndex(c => c.idCentro == centro.idCentro);
    //this.centros[indice] = centro;
    return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro.idCentro, centro);
  }

  eliminarcCentro(id: number) {
    //let indice = this.centros.findIndex(c => c.idCentro == id);
    //this.centros.splice(indice, 1);
    return this.httpClient.delete<void>(BACKEND_URI + '/centro/' + id);
  }

  getCentro(id: number): Observable<Centro> {
    return this.httpClient.get<Centro>(BACKEND_URI + '/centro/' + id);
  }
  */
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
}
