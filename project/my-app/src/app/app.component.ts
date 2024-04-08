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
import { Mensaje } from './mensaje';
import { MensajeService } from './mensaje.service';
import { EnviarMensaje } from './enviar-mensaje/enviar.mensaje.component';
import { LeerMensaje } from './leer-mensaje/leer-mensaje.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  // booleano provisional
  admin: boolean = false;

  centros: Centro [] = [];
  centroElegido?: Centro;
  centroSelect: boolean = false;
  centroSeleccionado?: Centro;

  gerentes: Gerente [] = [];
  gerenteElegido?: Gerente;
  gerenteSelect: boolean = false;
  gerenteSeleccionado?: Gerente;

  isButtonDisabled: boolean = true;

  mensajes: Mensaje[] = [];
  mensajeElegido?: Mensaje;
  mensajeSelect: boolean = false;
  mensajeSeleccionado?: Mensaje;

  constructor(private centrosService: CentrosService, private gerentesService: GerentesService, 
    private modalService: NgbModal, private mensajesService: MensajeService) { }

  async ngOnInit(): Promise<void> {
    this.centros = this.centrosService.getCentros();
    this.gerentes = this.gerentesService.getGerentes();
    this.mensajes = await this.mensajesService.getMensajes();
  }


  esAdmin(): boolean {
    return this.admin;
  }

// CENTROS

  elegirCentro(centro: Centro): void {
    //this.centroElegido = centro;
    this.centroSeleccionado = centro;
    this.centroSelect = true;
    this.isButtonDisabled=!(this.centroSelect&&this.gerenteSelect);
  }

  mostrarDetallesCentro(centro: Centro): void{
    let ref = this.modalService.open(DetalleCentroComponent);
    ref.componentInstance.centro = centro;
    /*AÑADIDO PARA EDITAR DATOS*/
    ref.componentInstance.centroEditado.subscribe((centroEditado: Centro) => {
      this.centrosService.editarCentro(centroEditado); // Actualizar el centro editado en el servicio
      this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.centroElegido = this.centros.find(c => c.idCentro === centroEditado.idCentro); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA ELIMINAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.centroEliminado.subscribe((idCentro: number) => {
      this.centrosService.eliminarcCentro(idCentro); // Eliminar el centro del servicio
      this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.centroElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
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
    this.modalService.dismissAll();
  }

  eliminarCentro(id: number): void {
    this.centrosService.eliminarcCentro(id);
    this.centros = this.centrosService.getCentros();
    this.centroElegido = undefined;
    this.modalService.dismissAll();
  }


// GERENTES

  elegirGerente(gerente: Gerente): void {
    //this.gerenteElegido = gerente;
    this.gerenteSeleccionado = gerente;
    this.gerenteSelect = true;
    this.isButtonDisabled=!(this.centroSelect&&this.gerenteSelect);
  }

  mostrarDetallesGerente(gerente: Gerente): void{
    let ref = this.modalService.open(DetalleGerenteComponent);
    ref.componentInstance.gerente = gerente;
    /*AÑADIDO PARA EDITAR DETALLES*/
    ref.componentInstance.gerenteEditado.subscribe((gerenteEditado: Gerente) => {
      this.gerentesService.editarGerente(gerenteEditado); // Actualizar el centro editado en el servicio
      this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.gerenteElegido = this.gerentes.find(c => c.idUsuario === gerenteEditado.idUsuario); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA BORRAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.gerenteEliminado.subscribe((idGerente: number) => {
      this.gerentesService.eliminargGerente(idGerente); // Eliminar el centro del servicio
      this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.gerenteElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
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
    this.modalService.dismissAll();
  }
  
  asociar(): void {
   if(!this.isButtonDisabled){
    
   }
  
  }


// MENSAJES

  elegirMensaje(mensaje: Mensaje): void {
    this.mensajeSeleccionado = mensaje;
    this.mensajeSelect = true;
  }

  aniadirMensaje(): void {
    let ref = this.modalService.open(EnviarMensaje);
    ref.result.then(() => {}, (reason) => {});
  }

  public eliminarMensaje(mensaje: number): void {
    this.mensajesService.eliminarMensaje(mensaje);
  }


  mostrarDetallesMensaje(mensaje: Mensaje): void{
    let ref = this.modalService.open(LeerMensaje);
    ref.componentInstance.mensaje = mensaje;
    
    /*AÑADIDO PARA BORRAR EL ELEMENTO DE LA LISTA
    ref.componentInstance.mensajeEliminado.subscribe((idGerente: number) => {
      this.gerentesService.eliminargGerente(idGerente); // Eliminar el centro del servicio
      this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.gerenteElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });*/
  }
}
