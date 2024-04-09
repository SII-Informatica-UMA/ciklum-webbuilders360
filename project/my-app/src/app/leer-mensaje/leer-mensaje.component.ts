import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Mensaje } from '../mensaje';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { GerentesService } from '../gerentes.service';
import { CommonModule } from '@angular/common';


@Component({
    selector: 'app-leer-mensaje',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './leer-mensaje.component.html',
    styleUrls: ['./leer-mensaje.component.css']
})
export class LeerMensajeComponent {
    @Input() mensaje?: Mensaje;
    @Output() mensajeEliminado = new EventEmitter<number>();

    constructor(private gerentesService: GerentesService, private modalService: NgbModal, public modal: NgbActiveModal) { }

    eliminarMensaje(): void {
        this.mensajeEliminado.emit(this.mensaje?.getIdMensaje());
        this.modal.close();
    }
}