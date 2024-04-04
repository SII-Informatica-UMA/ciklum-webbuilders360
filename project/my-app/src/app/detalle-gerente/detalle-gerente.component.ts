import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Gerente } from '../gerente';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { GerentesService } from '../gerentes.service';

@Component({
  selector: 'app-detalle-gerente',
  templateUrl: './detalle-gerente.component.html',
  styleUrls: ['./detalle-gerente.component.css']
})
export class DetalleGerenteComponent {
  @Input() gerente?: Gerente;
  @Output() gerenteEditado = new EventEmitter<Gerente>();
  @Output() gerenteEliminado = new EventEmitter<number>();

  constructor(private gerentesService: GerentesService, private modalService: NgbModal, public modal: NgbActiveModal) { }

  editarGerente(): void {
    let ref = this.modalService.open(FormularioGerenteComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.gerente = {...this.gerente};
    ref.result.then((gerente: Gerente) => {
      console.log();
      this.gerenteEditado.emit(gerente);
    }, (reason) => {});
    this.modal.close();
  }

  eliminarGerente(): void {
    this.gerenteEliminado.emit(this.gerente?.idUsuario);
    this.modal.close();
  }
}
