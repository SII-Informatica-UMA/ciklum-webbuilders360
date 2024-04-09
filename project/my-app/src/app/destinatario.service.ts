import { HttpClient } from "@angular/common/http";
import { firstValueFrom} from 'rxjs';
import { Destinatario } from "./destinatario";
import { DestinatarioDTO } from "./destinatario.dto";
import { EntrenadorDTO } from "./entrenador.dto";
import { UsuarioDTO } from "./usuario.dto";
import { ClienteDTO } from "./cliente.dto";
import { TiposDestinatarios } from "./tipos.destinatarios";
import { Injectable } from "@angular/core";
import { UsuariosService } from "./services/usuarios.service";

@Injectable({
    providedIn: 'root'
})
export class DestinatarioService {
    private clienteURL: string = 'http://localhost:8080/cliente?centro=';
    private entrenadorURL: string = 'http://localhost:8080/entrenador?centro=';
    private usuarioURL: string = 'http://localhost:8080/usuario/';
    private clientesID2Nombre: Map<number, string> = new Map();
    private entrenadoresID2Nombre: Map<number, string> = new Map();
    private nombre2Info: Map<string, DestinatarioDTO> = new Map();
    private centros: Map<number, string> = new Map();
    private nombres: string[] = [];

    constructor(private http: HttpClient, private usuariosService: UsuariosService) {
        this.inicializarArrays();
    }

    private async inicializarArrays() {
        let centroId: string | undefined = this.usuariosService._rolCentro?.centro?.toString();
        let [clientesDTO, entrenadoresDTO] =
            await Promise.all([firstValueFrom(this.http.get<ClienteDTO[]>(this.clienteURL + centroId)),
                               firstValueFrom(this.http.get<EntrenadorDTO[]>(this.entrenadorURL + centroId))]);
        this.procesarClientesDTO(clientesDTO);
        this.procesarEntrenadoresDTO(entrenadoresDTO);
    }

    private async procesarClientesDTO(clientesDTO: ClienteDTO[]) {
        for (let clienteDTO of clientesDTO) {
            let usuarioDTO: UsuarioDTO =
                await firstValueFrom(this.http.get<UsuarioDTO>(this.usuarioURL + clienteDTO.idUsuario.toString()));
            let nombre: string = this.procesarNombreUsuario(usuarioDTO)
            this.clientesID2Nombre.set(clienteDTO.idUsuario, nombre);
            this.nombre2Info.set(nombre, {id: clienteDTO.id, tipo: TiposDestinatarios.CLIENTE})
            this.procesarCentros();
        }
    }

    private async procesarEntrenadoresDTO(entrenadoresDTO: EntrenadorDTO[]) {
        for (let entrenadorDTO of entrenadoresDTO) {
            let usuarioDTO: UsuarioDTO =
                await firstValueFrom(this.http.get<UsuarioDTO>(this.usuarioURL + entrenadorDTO.idUsuario.toString()));
            let nombre: string = this.procesarNombreUsuario(usuarioDTO)
            this.entrenadoresID2Nombre.set(entrenadorDTO.idUsuario, nombre);
            this.nombre2Info.set(nombre, {id: entrenadorDTO.id, tipo: TiposDestinatarios.ENTRENADOR})
        }
    }

    private procesarCentros(): void {
        let nombre: string | undefined = this.usuariosService.rolCentro?.nombreCentro;
        let id: number | undefined = this.usuariosService.rolCentro?.centro;
        if (nombre !== undefined && id !== undefined) {
            this.nombre2Info.set(nombre, {id: id, tipo: TiposDestinatarios.CENTRO});
        }
    }

    private procesarNombreUsuario(usuarioDTO: UsuarioDTO): string {
        let nombre: string = (usuarioDTO.nombre + " " + usuarioDTO.apellido1 + " " + usuarioDTO.apellido2).trim();
        this.nombres.push(nombre);
        return nombre;
    }

    public destinatarioDTO2Destinatario(destinatarioDTO: DestinatarioDTO): Destinatario {
        switch(destinatarioDTO.tipo) {
            case TiposDestinatarios.CLIENTE:
                return this.getClienteDestinatario(destinatarioDTO.id);
            case TiposDestinatarios.ENTRENADOR:
                return this.getEntrenadorDestinatario(destinatarioDTO.id);
            default:
                return new Destinatario(-1, "", ""); // TODO cambiar por mensaje de error
        }
    }

    public getClienteDestinatario(id: number): Destinatario {
        let aux = this.clientesID2Nombre.get(id);
        if (aux == undefined) {
            aux = "";
        }
        return new Destinatario(id, aux, TiposDestinatarios.CLIENTE);
    }

    public getEntrenadorDestinatario(id: number): Destinatario {
        let aux = this.entrenadoresID2Nombre.get(id);
        if (aux == undefined) {
            aux = "";
        }
        return new Destinatario(id, aux, TiposDestinatarios.ENTRENADOR);
    }

    public destinatario2DestinatarioDTO(destinatario: Destinatario): DestinatarioDTO {
        return {id: destinatario.getID(), tipo: destinatario.getTipo()};
    }

    public getNombresDestinatarios(): readonly string[] {
        return this.nombres;
    }

    public nombres2DestinatariosDTO(nombres: string[]): DestinatarioDTO[] {
        // TODO Añadir mensaje de error (en caso de que no se compruebe antes que los nombres son correctos)
        let destinatariosDTO: DestinatarioDTO[] = [];
        for (let nombre of nombres) {
            let destinatarioDTO = this.nombre2Info.get(nombre);
            if (destinatarioDTO !== undefined) {
                destinatariosDTO.push(destinatarioDTO);
            }
        }
        return [];
    }
}