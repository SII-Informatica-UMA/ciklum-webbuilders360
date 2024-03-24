import { Component } from '@angular/core';
import  { Centro } from '../centro';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-formulario-centro',
  templateUrl: './formulario-centro.component.html',
  styleUrls: ['./formulario-centro.component.css']
})
export class FormularioCentroComponent {
  accion?: "Añadir" | "Editar";
  centro: Centro = {idCentro: 0, nombre: '', direccion: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarCentro(): void {
    this.modal.close(this.centro);
  }

}
