import { Injectable } from '@angular/core';
import { Mensaje } from './mensaje';

@Injectable({
  providedIn: 'root'
})
export class MensajesService {
  private mensajes: Mensaje [] = [
    {asunto: 'Reunion urgente', destinatarios: [{id:123, tipo:"GERENTE"}, {id:456, tipo:"CENTRO"}],
    copia: [{id:123, tipo:"GERENTE"}, {id:456, tipo:"CENTRO"}],
    copiaOculta: [{id:123, tipo:"GERENTE"}, {id:456, tipo:"CENTRO"}], 
    contenido: 'Mañana reunion URGENTE en la oficina central',
    idMensaje: 1 },

    {asunto: 'Cierre mañana', destinatarios: [{id:4545, tipo:"CENTRO"}, {id:456, tipo:"CENTRO"}],
    copia: [{id:4545, tipo:"CENTRO"}, {id:456, tipo:"CENTRO"}],
    copiaOculta: [{id:4545, tipo:"CENTRO"}, {id:456, tipo:"CENTRO"}], 
    contenido: 'Mañana cierre por obras',
    idMensaje: 2 }

  ];

  constructor() { }

  getMensajes(): Mensaje [] {
    return this.mensajes;
  }

  addMensaje(mensaje: Mensaje) {
    mensaje.idMensaje = this.mensajes.length + 1;
    this.mensajes.push(mensaje);
  }

  editarMensaje(mensaje: Mensaje) {
    let indice = this.mensajes.findIndex(c => c.idMensaje == mensaje.idMensaje);
    this.mensajes[indice] = mensaje;
  }

  eliminarmMensaje(id: number) {
    let indice = this.mensajes.findIndex(c => c.idMensaje == id);
    this.mensajes.splice(indice, 1);
  }
}
