import { Injectable } from '@angular/core';
import { Gerente } from './gerente';

@Injectable({
  providedIn: 'root'
})
export class GerentesService {
  private gerentes: Gerente [] = [
    {idUsuario: 1, empresa: 'Juan SL', id: 123},
    {idUsuario: 2, empresa: 'Paco SL', id: 456},
    {idUsuario: 3, empresa: 'Marta SL', id: 678}
  ];

  constructor() { }

  getGerentes(): Gerente [] {
    return this.gerentes;
  }

  addGerente(gerente: Gerente) {
    gerente.idUsuario = this.gerentes.length + 1;
    this.gerentes.push(gerente);
  }

  editarGerente(gerente: Gerente) {
    let indice = this.gerentes.findIndex(c => c.idUsuario == gerente.idUsuario);
    this.gerentes[indice] = gerente;
  }

  eliminargGerente(id: number) {
    let indice = this.gerentes.findIndex(c => c.idUsuario == id);
    this.gerentes.splice(indice, 1);
  }
}
