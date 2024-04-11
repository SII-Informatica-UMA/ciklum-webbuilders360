import { Injectable } from "@angular/core";
import { Login, UsuarioSesion, Rol, RolCentro } from "../entities/login";
import { Observable, of, forkJoin, concatMap, lastValueFrom } from "rxjs";
import {map} from 'rxjs/operators';
import * as jose from 'jose';

import { Usuario } from "../entities/usuario";
import { BackendService } from "./backend.service";
import { Gerente } from "../gerente";
import { Centro } from "../centro";
import { CentrosService } from "../centros.service";
import { GerentesService } from "../gerentes.service";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  _rolCentro?: RolCentro;

  constructor(private backend: BackendService,
              private centroService: CentrosService,
              private gerenteService: GerentesService) {}

  doLogin(login: Login): Observable<UsuarioSesion> {
    let jwtObs = this.backend.login(login.email, login.password);
    let usuarioObs = jwtObs.pipe(concatMap(jwt=>this.backend.getUsuario(this.getUsuarioIdFromJwt(jwt))));
    let join = forkJoin({jwt: jwtObs, usuario: usuarioObs});
    let usuarioSesion = join.pipe(map(obj => {
      return {
        id: obj.usuario.id,
        nombre: obj.usuario.nombre,
        apellido1: obj.usuario.apellido1,
        apellido2: obj.usuario.apellido2,
        email: obj.usuario.email,
        roles: obj.usuario.administrador?[{rol: Rol.ADMINISTRADOR}]:[],
        jwt: obj.jwt
      };
    }));
    return usuarioSesion
    .pipe(concatMap(usuarioSesion=>this.completarConRoles(usuarioSesion)))
    .pipe(map(usuarioSesion => {
      localStorage.setItem('usuario', JSON.stringify(usuarioSesion));
      if (usuarioSesion.roles.length > 0) {
        this.rolCentro = usuarioSesion.roles[0];
      } else {
        this.rolCentro = undefined;
      }
      return usuarioSesion;
    }));

  }

  private completarConRoles(usuarioSesion: UsuarioSesion): Observable<UsuarioSesion> {
    return this.getGerente(usuarioSesion)
      .pipe(concatMap((gerente?: Gerente) => this.getCentros(gerente)))
      .pipe(map((centros: Centro[]) => this.aniadirRolesGerente(usuarioSesion, centros)));
  }

  private getGerente(usuarioSesion: UsuarioSesion): Observable<Gerente | undefined> {
    return this.gerenteService.getGerentes().pipe(map((gerentes: Gerente[]) => {
      for (let gerente of gerentes) {
        if (gerente.idUsuario == usuarioSesion.id) {
          return gerente;
        }
      }
      return undefined;
    }));
  }

  private getCentros(gerente?: Gerente): Observable<Centro[]> {
    if (gerente !== undefined) {
      return this.centroService.getCentros(gerente.id);
    } else {
      return of([]);
    }
  }

  private aniadirRolesGerente(usuarioSesion: UsuarioSesion, centros: Centro[]): UsuarioSesion {
    centros.forEach(centro => {
      usuarioSesion.roles.push({
        rol: Rol.GERENTE,
        centro: centro.idCentro,
        nombreCentro: centro.nombre
      })
    })
    return usuarioSesion;
  }

  private mapRoles(centros: Centro[]): RolCentro[] {
    return centros.map((centro: Centro) => {
        return {
        rol: Rol.GERENTE,
        centro: centro.idCentro,
        nombreCentro: centro.nombre
      }
    });
  }

  private getUsuarioIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: "+JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  get rolCentro(): RolCentro | undefined {
    return this._rolCentro;
  }

  set rolCentro(r: RolCentro | undefined) {
    this._rolCentro = r;
  }

  getUsuarioSesion(): UsuarioSesion | undefined {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : undefined;
  }

  doLogout() {
    localStorage.removeItem('usuario');
  }

  doForgottenPassword(email: string): Observable<void> {
    return this.backend.forgottenPassword(email);
  }

  doCambiarContrasenia(password: string, token: string): Promise<void> {
    return lastValueFrom(this.backend.resetPassword(token, password),{defaultValue:undefined});
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.backend.getUsuarios();
  }

  editarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.putUsuario(usuario);
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.backend.deleteUsuario(id);
  }

  aniadirUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.postUsuario(usuario);
  }
}