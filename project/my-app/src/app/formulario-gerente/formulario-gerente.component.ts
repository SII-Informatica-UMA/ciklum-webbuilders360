import { Component } from '@angular/core';
import  { Gerente } from '../gerente'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-formulario-gerente',
  standalone: true,
  imports: [FormsModule],
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
