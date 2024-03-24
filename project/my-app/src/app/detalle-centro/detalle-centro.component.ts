import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Centro } from '../centro';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';
import { CentrosService } from '../centros.service';

@Component({
  selector: 'app-detalle-centro',
  templateUrl: './detalle-centro.component.html',
  styleUrls: ['./detalle-centro.component.css']
})
export class DetalleCentroComponent {
  @Input() centro?: Centro;
  @Output() centroEditado = new EventEmitter<Centro>();
  @Output() centroEliminado = new EventEmitter<number>();

  constructor(private centrosService: CentrosService, private modalService: NgbModal) { }

  editarCentro(): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.centro = {...this.centro};
    ref.result.then((centro: Centro) => {
      this.centroEditado.emit(centro);
    }, (reason) => {});
  }

  eliminarCentro(): void {
    this.centroEliminado.emit(this.centro?.idCentro);
  }
}
