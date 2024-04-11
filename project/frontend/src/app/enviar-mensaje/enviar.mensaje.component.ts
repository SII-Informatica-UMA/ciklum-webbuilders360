import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MensajeService } from '../mensaje.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { AsyncPipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NgFor } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UsuariosService } from '../services/usuarios.service';
import { CdkListbox, CdkOption } from '@angular/cdk/listbox';
import { JsonPipe } from '@angular/common';
import { RolCentro } from '../entities/login';

@Component({
    selector: 'enviar-mensaje',
    standalone: true,
    imports: [MatInputModule, ReactiveFormsModule, AsyncPipe, FormsModule, MatFormFieldModule, MatAutocompleteModule, NgFor, CdkListbox, CdkOption, JsonPipe],
    templateUrl: './enviar.mensaje.component.html',
    styleUrls: ['./enviar.mensaje.component.css']
})
export class EnviarMensaje {
    asunto: string = "";
    contenido: string = "";
    destinatarios: FormControl;
    copia: FormControl;
    copiaOculta: FormControl;
    nombresDestinatarios: Set<string> = new Set<string>();

    public constructor(public modal: NgbActiveModal,
                       private mensajeService: MensajeService,
                       private usuariosService: UsuariosService) {
        this.cargarDestinatarios();
        this.destinatarios = new FormControl();
        this.copia = new FormControl();
        this.copiaOculta = new FormControl();
    }

    private async cargarDestinatarios() {
        this.nombresDestinatarios = await this.mensajeService.getNombresDestinatarios();
    }

    public async enviarMensaje() {
        let centro : RolCentro | undefined = this.usuariosService.rolCentro;
        if (centro === undefined || centro.nombreCentro === undefined || centro.centro === undefined) {
            throw "Centro debe estar definido";
        }
        await this.mensajeService.enviarMensaje(this.asunto,
                                            this.destinatarios.value || [],
                                            this.copia.value || [],
                                            this.copiaOculta.value || [],
                                            centro.nombreCentro,
                                            this.contenido,
                                            centro.centro);
        this.modal.close();
      }
}