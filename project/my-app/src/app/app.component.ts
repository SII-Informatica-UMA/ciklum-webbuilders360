import { Component, OnInit } from '@angular/core';
import { Centro } from './centro';
import { Gerente } from './gerente';
import { CentrosService } from './centros.service';
import { GerentesService } from './gerentes.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component'
import { FormularioGerenteComponent } from './formulario-gerente/formulario-gerente.component';
import { DetalleCentroComponent } from './detalle-centro/detalle-centro.component';
import { DetalleGerenteComponent } from './detalle-gerente/detalle-gerente.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  centros: Centro [] = [];
  centroElegido?: Centro;

  gerentes: Gerente [] = [];
  gerenteElegido?: Gerente;

  constructor(private centrosService: CentrosService, private gerentesService: GerentesService, 
    private modalService: NgbModal) { }

  ngOnInit(): void {
    this.centros = this.centrosService.getCentros();
    this.gerentes = this.gerentesService.getGerentes();
  }

  elegirCentro(centro: Centro): void {
    this.centroElegido = centro;
    let ref = this.modalService.open(DetalleCentroComponent);
    ref.componentInstance.centro = centro;
  }

  aniadirCentro(): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.centro = {idCentro: 0, nombre: '', direccion: ''};
    ref.result.then((centro: Centro) => {
      this.centrosService.addCentro(centro);
      this.centros = this.centrosService.getCentros();
    }, (reason) => {});

  }
  centroEditado(centro: Centro): void {
    this.centrosService.editarCentro(centro);
    this.centros = this.centrosService.getCentros();
    this.centroElegido = this.centros.find(c => c.idCentro == centro.idCentro);
  }

  eliminarCentro(id: number): void {
    this.centrosService.eliminarcCentro(id);
    this.centros = this.centrosService.getCentros();
    this.centroElegido = undefined;
  }


  elegirGerente(gerente: Gerente): void {
    this.gerenteElegido = gerente;
    let ref = this.modalService.open(DetalleGerenteComponent);
    ref.componentInstance.gerente = gerente;
  }

  aniadirGerente(): void {
    let ref = this.modalService.open(FormularioGerenteComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.gerente = {idUsuario: 0, empresa: '', id: 0};
    ref.result.then((gerente: Gerente) => {
      this.gerentesService.addGerente(gerente);
      this.gerentes = this.gerentesService.getGerentes();
    }, (reason) => {});

  }
  gerenteEditado(gerente: Gerente): void {
    this.gerentesService.editarGerente(gerente);
    this.gerentes = this.gerentesService.getGerentes();
    this.gerenteElegido = this.gerentes.find(c => c.idUsuario == gerente.idUsuario);
  }

  eliminarGerente(id: number): void {
    this.gerentesService.eliminargGerente(id);
    this.gerentes = this.gerentesService.getGerentes();
    this.gerenteElegido = undefined;
  }
  

}
