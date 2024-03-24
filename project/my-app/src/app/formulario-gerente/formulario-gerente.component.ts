import { Component } from '@angular/core';
import  { Gerente } from '../gerente'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-formulario-gerente',
  templateUrl: './formulario-gerente.component.html',
  styleUrls: ['./formulario-gerente.component.css']
})
export class FormularioGerenteComponent {
  accion?: "Añadir" | "Editar";
  gerente: Gerente = {idUsuario: 0, empresa: '', id: 0};

  constructor(public modal: NgbActiveModal) { }

  guardarGerente(): void {
    this.modal.close(this.gerente);
  }

}
