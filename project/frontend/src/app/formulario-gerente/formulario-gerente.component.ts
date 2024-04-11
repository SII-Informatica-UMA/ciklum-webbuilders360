import { Component } from '@angular/core';
import  { Gerente } from '../gerente'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../entities/usuario';
import { UsuariosService } from '../services/usuarios.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formulario-gerente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formulario-gerente.component.html',
  styleUrls: ['./formulario-gerente.component.css']
})
export class FormularioGerenteComponent {
  accion?: "Añadir" | "Editar";
  gerente: Gerente = {idUsuario: 0, empresa: '', id: 0};
  usuarios: Usuario [] = [];
  usuarioSeleccionado?: Usuario;

  constructor(public modal: NgbActiveModal,
              private usuariosService: UsuariosService) { 

  }

  async ngOnInit(): Promise<void>{
    this.actualizarUsuarios();
  }

  guardarGerente(): void {
    if(this.gerente && this.usuarioSeleccionado){
      this.gerente.idUsuario = this.usuarioSeleccionado?.id;
    }
    this.modal.close(this.gerente);
  }

  actualizarUsuarios() {
    this.usuariosService.getUsuarios().subscribe(usuarios => {
      this.usuarios = usuarios;
    });
  }

  elegirUsuario(usuario: Usuario): void {
    this.usuarioSeleccionado = usuario;
  }
}
