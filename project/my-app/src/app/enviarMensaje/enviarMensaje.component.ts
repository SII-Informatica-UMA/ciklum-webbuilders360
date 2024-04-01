import { Component } from '@angular/core';
import { FormControl, FormsModule, Validators, FormGroup } from '@angular/forms';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';
import { MensajeService } from '../mensaje.service';
import {MatAutocompleteModule} from '@angular/material/autocomplete';

@Component({
    selector: 'enviarMensaje',
    standalone: true,
    imports: [FormsModule, MatAutocompleteModule],
    templateUrl: './enviarMensaje.component.html',
    styleUrls: ['./enviarMensaje.component.css']
})

export class EnviarMensaje {
    asunto: string = "";
    contenido: string = "";
    destinatarios: Destinatario[] = [];
    copia: Destinatario[] = [];
    copiaOculta: Destinatario[] = [];
    nombresDestinatarios: readonly string[];

    fromGroup = new FormGroup({control: new FormControl()});

    public constructor(private mensajeService: MensajeService) {
        this.nombresDestinatarios = mensajeService.getNombresDestinatarios();
    }

    public async enviarMensaje() {
        this.mensajeService.enviarMensaje({asunto: this.asunto,
            destinatarios: this.mensajeService.destinatarios2DestinatariosDTO(this.destinatarios),
            copia: this.mensajeService.destinatarios2DestinatariosDTO(this.copia),
            copiaOculta: this.mensajeService.destinatarios2DestinatariosDTO(this.copiaOculta),
            contenido: this.contenido});
    }
}