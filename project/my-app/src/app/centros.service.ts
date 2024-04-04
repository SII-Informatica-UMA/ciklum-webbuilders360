import { Injectable } from '@angular/core';
import { Centro } from './centro';

@Injectable({
  providedIn: 'root'
})
export class CentrosService {
  private centros: Centro [] = [
    {idCentro: 1, nombre: 'JuanGym', direccion: 'c/calle 123'},
    {idCentro: 2, nombre: 'AnaGimnasio', direccion: 'c/calle 321'},
    {idCentro: 3, nombre: 'PacoGym', direccion: 'c/calle 4'}
  ];

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
