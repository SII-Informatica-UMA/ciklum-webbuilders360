import { Component, OnInit } from '@angular/core';
import { Centro } from '../centro';
import { Gerente } from '../gerente';
import { CentrosService } from '../centros.service';
import { GerentesService } from '../gerentes.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component'
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { DetalleCentroComponent } from '../detalle-centro/detalle-centro.component';
import { DetalleGerenteComponent } from '../detalle-gerente/detalle-gerente.component';
import { Mensaje } from '../mensaje';
import { MensajeService } from '../mensaje.service';
import { EnviarMensaje } from '../enviar-mensaje/enviar.mensaje.component';
import { LeerMensajeComponent } from '../leer-mensaje/leer-mensaje.component';
import { CommonModule } from '@angular/common';
import { Rol } from '../entities/login';
import { UsuariosService } from '../services/usuarios.service';
import { AsociacionComponent } from '../asociacion/asociacion.component';


@Component({
  selector: 'app-centros',
  standalone: true,
  imports: [CommonModule, LeerMensajeComponent, DetalleCentroComponent, DetalleGerenteComponent],
  templateUrl: './centros.component.html',
  styleUrls: ['./centros.component.css']
})
export class CentrosComponent implements OnInit {

  centros: Centro [] = [];
  centroElegido?: Centro;
  centroSelect: boolean = false;
  centroSeleccionado?: Centro;

  gerentes: Gerente [] = [];
  gerenteElegido?: Gerente;
  gerenteSelect: boolean = false;
  gerenteSeleccionado?: Gerente;

  asociacion: Map<Centro, Gerente> = new Map<Centro, Gerente>;
  asociacionRealizada: Boolean = false;
  isButtonDisabled: boolean = true;

  mensajes: Mensaje[] = [];
  mensajeElegido?: Mensaje;
  mensajeSelect: boolean = false;
  mensajeSeleccionado?: Mensaje;

  constructor(private centrosService: CentrosService,
              private gerentesService: GerentesService,
              private modalService: NgbModal,
              private mensajesService: MensajeService,
              private usuariosService: UsuariosService) {
  }

  async ngOnInit(): Promise<void> {
    //this.centros = this.centrosService.getCentros();
    //this.gerentes = this.gerentesService.getGerentes();
    // Hecho con el backend
    this.actualizarCentros();
    if (this.esAdmin()) {
      //this.gerentes = this.gerentesService.getGerentes();
      this.actualizarGerentes();
      this.asociacion;
    } else {
      this.mensajes = await this.mensajesService.getMensajes();
    }
  }


  esAdmin(): boolean {
    return this.usuariosService.rolCentro?.rol==Rol.ADMINISTRADOR;
  }

// CENTROS
  // CON EL BACKEND
  actualizarCentros() {
    this.centrosService.getCentros().subscribe(centros => {
      this.centros = centros;
    });
  }

  
  elegirCentro(centro: Centro): void {
    //this.centroElegido = centro;
    this.centroSeleccionado = centro;
    this.centroSelect = true;
    this.isButtonDisabled=!(this.centroSelect&&this.gerenteSelect);
  }

  mostrarDetallesCentro(centro: Centro): void{
    const asociado = this.asociacion.get(centro);
    let ref = this.modalService.open(DetalleCentroComponent);
    ref.componentInstance.centro = centro;
    ref.componentInstance.gerente = asociado;
    /*AÑADIDO PARA EDITAR DATOS*/
    ref.componentInstance.centroEditado.subscribe((centroEditado: Centro) => {
      this.centrosService.editarCentro(centroEditado); // Actualizar el centro editado en el servicio
      //this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.actualizarCentros(); // con el backend
      this.centroElegido = this.centros.find(c => c.idCentro === centroEditado.idCentro); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA ELIMINAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.centroEliminado.subscribe((idCentro: number) => {
      this.centrosService.eliminarcCentro(idCentro); // Eliminar el centro del servicio
      //this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.actualizarCentros();  // con el backend
      this.centroElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
  }

  detallesCentro(): void{
    const asociado = this.getGerenteAsociado();
    let ref = this.modalService.open(DetalleCentroComponent);
    ref.componentInstance.centro = this.centroSeleccionado;
    ref.componentInstance.gerente = asociado;
    /*AÑADIDO PARA EDITAR DATOS*/
    ref.componentInstance.centroEditado.subscribe((centroEditado: Centro) => {
      this.centrosService.editarCentro(centroEditado); // Actualizar el centro editado en el servicio
      //this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.actualizarCentros(); // con el backend
      this.centroElegido = this.centros.find(c => c.idCentro === centroEditado.idCentro); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA ELIMINAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.centroEliminado.subscribe((idCentro: number) => {
      this.centrosService.eliminarcCentro(idCentro); // Eliminar el centro del servicio
      //this.centros = this.centrosService.getCentros(); // Actualizar la lista de centros en el componente
      this.actualizarCentros(); // con el backend
      this.centroElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
  }

  aniadirCentro(): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.centro = {idCentro: 0, nombre: '', direccion: ''};
    ref.result.then((centro: Centro) => {
      this.centrosService.addCentro(centro);
      //this.centros = this.centrosService.getCentros();
      this.actualizarCentros(); // con el backend
    }, (reason) => {});

  }
  centroEditado(centro: Centro): void {
    this.centrosService.editarCentro(centro);
    //this.centros = this.centrosService.getCentros();
    this.actualizarCentros();  // con el backend
    this.centroElegido = this.centros.find(c => c.idCentro == centro.idCentro);
    this.modalService.dismissAll();
  }

  eliminarCentro(id: number): void {
    this.centrosService.eliminarcCentro(id);
    //this.centros = this.centrosService.getCentros();
    this.actualizarCentros();  // con el backend
    this.centroElegido = undefined;
    this.modalService.dismissAll();
  }


// GERENTES
  // Hecho con el backend
  actualizarGerentes() {
    this.gerentesService.getGerentes().subscribe(gerentes => {
      this.gerentes = gerentes;
    });
  }

  elegirGerente(gerente: Gerente): void {
    this.gerenteSeleccionado = gerente;
    this.gerenteSelect = true;
    this.isButtonDisabled=!(this.centroSelect&&this.gerenteSelect);
  }

  mostrarDetallesGerente(gerente: Gerente): void{
    const centrosAsociados = this.getCentrosAsociados();
    let ref = this.modalService.open(DetalleGerenteComponent);
    ref.componentInstance.gerente = gerente;
    ref.componentInstance.centrosAsociados = centrosAsociados;
    /*AÑADIDO PARA EDITAR DETALLES*/
    ref.componentInstance.gerenteEditado.subscribe((gerenteEditado: Gerente) => {
      this.gerentesService.editarGerente(gerenteEditado); // Actualizar el centro editado en el servicio
      //this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.actualizarGerentes(); //Actualizar con el backend
      this.gerenteElegido = this.gerentes.find(c => c.idUsuario === gerenteEditado.idUsuario); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA BORRAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.gerenteEliminado.subscribe((idGerente: number) => {
      this.gerentesService.eliminargGerente(gerente.id); // Eliminar el centro del servicio
      //this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.actualizarGerentes(); // Actualizar con el backend
      this.gerenteElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
  }

  detallesGerente(): void{
    const centrosAsociados = this.getCentrosAsociados();
    let ref = this.modalService.open(DetalleGerenteComponent);
    ref.componentInstance.gerente = this.gerenteSeleccionado;
    ref.componentInstance.centrosAsociados = centrosAsociados;
    /*AÑADIDO PARA EDITAR DETALLES*/
    ref.componentInstance.gerenteEditado.subscribe((gerenteEditado: Gerente) => {
      this.gerentesService.editarGerente(gerenteEditado); // Actualizar el centro editado en el servicio
      //this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      this.actualizarGerentes(); // Actualizar con el backend
      this.gerenteElegido = this.gerentes.find(c => c.idUsuario === gerenteEditado.idUsuario); // Actualizar el centro elegido
    });
    /*AÑADIDO PARA BORRAR EL ELEMENTO DE LA LISTA*/
    ref.componentInstance.gerenteEliminado.subscribe((idGerente: number) => {
      if(this.gerenteSeleccionado){
        this.gerentesService.eliminargGerente(this.gerenteSeleccionado?.id); // Eliminar el centro del servicio
      //this.gerentes = this.gerentesService.getGerentes(); // Actualizar la lista de centros en el componente
      }
      this.actualizarGerentes(); // Actualizar con el backend
      this.gerenteElegido = undefined; // Limpiar el centro elegido si fue eliminado
    });
  }

  aniadirGerente(): void {
    let ref = this.modalService.open(FormularioGerenteComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.gerente = {idUsuario: 0, empresa: '', id: 0};
    ref.result.then((gerente: Gerente) => {
      this.gerentesService.addGerente(gerente);
      //this.gerentes = this.gerentesService.getGerentes();
      this.actualizarGerentes(); // Actualizar con el backend
    }, (reason) => {});

  }
  gerenteEditado(gerente: Gerente): void {
    this.gerentesService.editarGerente(gerente);
    //this.gerentes = this.gerentesService.getGerentes();
    this.actualizarGerentes(); // con el backend
    this.gerenteElegido = this.gerentes.find(c => c.idUsuario == gerente.idUsuario);
  }

  eliminarGerente(id: number): void {
    this.gerentesService.eliminargGerente(id);
    //this.gerentes = this.gerentesService.getGerentes();
    this.actualizarGerentes(); // actualizar con el backend
    this.gerenteElegido = undefined;
    this.modalService.dismissAll();
  }
  
  asociar(): void {
    if(!this.isButtonDisabled){
      if(this.centroSeleccionado && this.gerenteSeleccionado){
        if(!this.asociacion.has(this.centroSeleccionado)){
          this.asociacion.set(this.centroSeleccionado, this.gerenteSeleccionado);
          this.asociacionRealizada = true;
        }else{
          this.asociacionRealizada = false;
        }
      }
    }
    if(this.asociacionRealizada){
      this.mostrarModalAsociacion('Asociación realizada entre el gerente y el centro seleccionado');
    }else{
      this.mostrarModalAsociacion('Error en la asociacion');
    }
  }

  mostrarModalAsociacion(mensaje: String): void{
    let ref = this.modalService.open(AsociacionComponent);
    ref.componentInstance.mensaje = mensaje;
  }

  getCentrosAsociados(): Centro[]{
    const centrosAsociados: Centro[] = [];
    this.asociacion.forEach((g, c) =>{
      if(g === this.gerenteSeleccionado){
        centrosAsociados.push(c);
      }
    });
    return centrosAsociados;
  }

  getGerenteAsociado(): Gerente | undefined{
    let gerenteAsociado: Gerente | undefined;
    if(this.centroSeleccionado && this.asociacion.has(this.centroSeleccionado)){
      gerenteAsociado = this.asociacion.get(this.centroSeleccionado);
    }
    return gerenteAsociado;
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

  public async eliminarMensaje(mensaje: number): Promise<void> {
    this.mensajesService.eliminarMensaje(mensaje);
    this.mensajes = await this.mensajesService.getMensajes();
    this.mensajeElegido = undefined;
    this.modalService.dismissAll();
  }


  mostrarDetallesMensaje(mensaje: Mensaje): void{
    let ref = this.modalService.open(LeerMensajeComponent);
    ref.componentInstance.mensaje = mensaje;
  }
}
