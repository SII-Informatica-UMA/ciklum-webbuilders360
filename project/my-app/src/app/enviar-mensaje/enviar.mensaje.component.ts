import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MensajeService } from '../mensaje.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { AsyncPipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NgFor } from '@angular/common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'enviar-mensaje',
    standalone: true,
    imports: [MatInputModule, ReactiveFormsModule, AsyncPipe, FormsModule, MatFormFieldModule, MatAutocompleteModule, NgFor],
    templateUrl: './enviar.mensaje.component.html',
    styleUrls: ['./enviar.mensaje.component.css']
})
export class EnviarMensaje {
    asunto: string = "";
    contenido: string = "";
    destinatarios: string[] = [""];
    copia: string[] = [""];
    copiaOculta: string[] = [""];
    nombresDestinatarios: readonly string[];

    @ViewChild('inputDestinatario') inputDestinatario!: ElementRef<HTMLInputElement>;
    myControlDestinatario = new FormControl('');
    filteredOptionsDestinatario: string[];

    @ViewChild('inputCopia') inputCopia!: ElementRef<HTMLInputElement>;
    myControlCopia = new FormControl('');
    filteredOptionsCopia: string[];

    @ViewChild('inputCopiaOculta') inputCopiaOculta!: ElementRef<HTMLInputElement>;
    myControlCopiaOculta = new FormControl('');
    filteredOptionsCopiaOculta: string[];

    public constructor(public modal: NgbActiveModal, private mensajeService: MensajeService) {
        this.nombresDestinatarios = mensajeService.getNombresDestinatarios();
        this.filteredOptionsDestinatario = this.nombresDestinatarios.slice();
        this.filteredOptionsCopia = this.nombresDestinatarios.slice();
        this.filteredOptionsCopiaOculta = this.nombresDestinatarios.slice();
    }

    public async enviarMensaje() {
        this.mensajeService.enviarMensaje(this.asunto, this.destinatarios, this.copia, this.copiaOculta, this.contenido);
        this.modal.close();
      }

    public filterDestinatario(): void {
        const filterValue = this.inputDestinatario.nativeElement.value.toLowerCase();
        this.filteredOptionsDestinatario = this.nombresDestinatarios.filter(o => o.toLowerCase().includes(filterValue));
    }

    public filterCopia(): void {
        const filterValue = this.inputCopia.nativeElement.value.toLowerCase();
        this.filteredOptionsCopia = this.nombresDestinatarios.filter(o => o.toLowerCase().includes(filterValue));
    }

    public filterCopiaOculta(): void {
        const filterValue = this.inputCopiaOculta.nativeElement.value.toLowerCase();
        this.filteredOptionsCopiaOculta = this.nombresDestinatarios.filter(o => o.toLowerCase().includes(filterValue));
    }

    public addInputDestinatario(): void {
        this.destinatarios.push("");
    }

    public addInputCopia(): void {
        this.copia.push("");
    }

    public addInputCopiaOculta(): void {
        this.copiaOculta.push("");
    }
}