import { Component } from '@angular/core';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';


@Component({
    selector: 'leerMensaje',
    templateUrl: './leerMensaje.component.html',
    styleUrls: ['./leerMensaje.component.css']
})

export class LeerMensaje {
    constructor(private mensaje: Mensaje) {

    }

    get mensajeContenido(): string {
        return this.mensaje.contenido;
    }

    get mensajeDestinatarios(): readonly string[] {
        return this.mensaje.listaNombresDestinatarios;
    }

    get mensajeCopia(): readonly Destinatario[] {
        return this.mensaje.listaCopia;
    }

    get mensajeCopiaOculta(): readonly Destinatario[] {
        return this.mensaje.listaCopiaOculta;
    }

    get mensajeAsunto(): string {
        return this.mensaje.asunto;
    }
}