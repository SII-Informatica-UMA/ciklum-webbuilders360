import { Component, Input } from '@angular/core';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';
import { MensajeService } from "../mensaje.service";


@Component({
    selector: 'leer-mensaje',
    templateUrl: './leer.mensaje.component.html',
    styleUrls: ['./leer.mensaje.component.css']
})

export class LeerMensaje {
    @Input() mensaje?: Mensaje;

    constructor() {}
}