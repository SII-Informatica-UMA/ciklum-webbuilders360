import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Mensaje } from '../mensaje';
import { Destinatario } from '../destinatario';
import { MensajeService } from "../mensaje.service";
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { GerentesService } from '../gerentes.service';
import { Gerente } from '../gerente';


@Component({
    selector: 'app-leer-mensaje',
    templateUrl: './leer-mensaje.component.html',
    styleUrls: ['./leer-mensaje.component.css']
})

export class LeerMensajeComponent {
    @Input() mensaje?: Mensaje;
    @Output() gerenteEditado = new EventEmitter<Gerente>();
    @Output() gerenteEliminado = new EventEmitter<number>();

    constructor(private gerentesService: GerentesService, private modalService: NgbModal, public modal: NgbActiveModal) { }

    eliminarGerente(): void {
        this.gerenteEliminado.emit(this.mensaje?.getIdMensaje());
        this.modal.close();
    }
}