import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Gerente } from '../gerente';
import { Centro } from '../centro';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { GerentesService } from '../gerentes.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Usuario } from '../entities/usuario';
import { UsuariosService } from '../services/usuarios.service';


@Component({
  selector: 'app-detalle-gerente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detalle-gerente.component.html',
  styleUrls: ['./detalle-gerente.component.css']
})
export class DetalleGerenteComponent {
  @Input() gerente?: Gerente;
  @Input() usuario?: Usuario;
  @Input() centrosAsociados? : Centro[];
  @Output() gerenteEditado = new EventEmitter<Gerente>();
  @Output() gerenteEliminado = new EventEmitter<number>();

  constructor(private gerentesService: GerentesService, private modalService: NgbModal, public modal: NgbActiveModal,
    private usuariosService: UsuariosService
  ) { }

  async ngOnInit(): Promise<void> {
    let usuarios: Usuario [] = [];
    this.usuariosService.getUsuarios().subscribe(users => {
      usuarios = users;
    })
    if (this.gerente){
      this.buscarUsuario(this.gerente?.idUsuario, usuarios);
    }
    
  }
  editarGerente(): void {
    let ref = this.modalService.open(FormularioGerenteComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.gerente = {...this.gerente};
    ref.result.then((gerente: Gerente) => {
      this.gerenteEditado.emit(gerente);
    }, (reason) => {});
    this.modal.close();
  }

  eliminarGerente(): void {
    this.gerenteEliminado.emit(this.gerente?.idUsuario);
    this.modal.close();
  }

  buscarUsuario(idUsuario: number, users: Usuario[]): void {
    for (let i=0; i<users.length;i++) {
      if (idUsuario==users[i].id) this.usuario=users[i];
    }
  }
}
