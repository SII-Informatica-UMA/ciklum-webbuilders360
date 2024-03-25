import { Injectable } from '@angular/core';
import { Gerente } from './gerente';

@Injectable({
  providedIn: 'root'
})
export class GerentesService {
  private gerentes: Gerente [] = [
    {idUsuario: 1, empresa: 'Juan SL', id: 123, nombre: 'Juan', apellido1:'Garcia', apellido2:'Rodriguez', email:'juan@correo'},
    {idUsuario: 2, empresa: 'Paco SL', id: 456, nombre: 'Paco', apellido1:'Rodriguez', apellido2:'Muñoz', email:'paco@correo'},
    {idUsuario: 3, empresa: 'Marta SL', id: 678, nombre: 'Marta', apellido1:'Muñoz', apellido2:'Garcia', email:'marta@correo'}
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
