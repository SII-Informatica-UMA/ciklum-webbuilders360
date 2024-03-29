import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';

@Component({
    selector: 'enviarMensaje',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './enviarMensaje.component.html',
    styleUrls: ['./enviarMensaje.component.css']
})

export class EnviarMensaje {
    asunto: string = "";
    contenido: string = "";
}