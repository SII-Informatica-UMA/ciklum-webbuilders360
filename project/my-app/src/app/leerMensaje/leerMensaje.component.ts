import { Component } from '@angular/core';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';
import { MensajeService } from "../mensaje.service";


@Component({
    selector: 'leerMensaje',
    templateUrl: './leerMensaje.component.html',
    styleUrls: ['./leerMensaje.component.css']
})

export class LeerMensaje {
    constructor(private mensaje: Mensaje, private mensajeService: MensajeService) {

    }

    public getMensaje(): Mensaje {
        return this.mensaje;
    }

    public eliminarMensaje(): void {
        this.mensajeService.eliminarMensaje(this.mensaje.getIdMensaje());
    }
}