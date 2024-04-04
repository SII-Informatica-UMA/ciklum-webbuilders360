import { HttpClient } from "@angular/common/http";
import { firstValueFrom} from 'rxjs';
import { Destinatario } from "./destinatario";

export class DestinatarioService {
    private clienteURL: string = 'http://localhost:8080/cliente?centro=';
    private entrenadorURL: string = 'http://localhost:8080/entrenador?centro=';
    private usuarioURL: string = 'http://localhost:8080/usuario/';
    private clientes: Map<number, string> = new Map();
    private entrenadores: Map<number, string> = new Map();
    private centros: Map<number, string> = new Map(); // TODO Añadir funcionalidad con los centros
    private nombres: string[] = [];

    constructor(private http: HttpClient, centrosID: number[]) {
        this.inicializarArrays(centrosID);
    }

    private async inicializarArrays(centrosID: number[]) {
        for (let centroID of centrosID) {
            let [clientesDTO, entrenadoresDTO] =
                await Promise.all([firstValueFrom(this.http.get<ClienteDTO[]>(this.clienteURL + centroID.toString())),
                                   firstValueFrom(this.http.get<EntrenadorDTO[]>(this.entrenadorURL + centroID.toString()))]);
            this.procesarClientesDTO(clientesDTO);
            this.procesarEntrenadoresDTO(entrenadoresDTO);
        }
    }

    private async procesarClientesDTO(clientesDTO: ClienteDTO[]) {
        for (let clienteDTO of clientesDTO) {
            let usuarioDTO: UsuarioDTO =
                await firstValueFrom(this.http.get<UsuarioDTO>(this.usuarioURL + clienteDTO.idUsuario.toString()));
            this.clientes.set(clienteDTO.idUsuario, usuarioDTO.email);
        }
    }

    private async procesarEntrenadoresDTO(entrenadoresDTO: EntrenadorDTO[]) {
        for (let entrenadorDTO of entrenadoresDTO) {
            let usuarioDTO: UsuarioDTO =
                await firstValueFrom(this.http.get<UsuarioDTO>(this.usuarioURL + entrenadorDTO.idUsuario.toString()));
            this.entrenadores.set(entrenadorDTO.idUsuario, usuarioDTO.email);
        }
    }

    private procesarNombreUsuario(usuarioDTO: UsuarioDTO): string {
        // TODO Añadir posibilidad de no tener segundo apellido
        let nombre: string = usuarioDTO.nombre + " " + usuarioDTO.apellido1 + " " + usuarioDTO.apellido2;
        this.nombres.push;
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
        let aux = this.clientes.get(id);
        if (aux == undefined) {
            aux = "";
        }
        return new Destinatario(id, aux, TiposDestinatarios.CLIENTE);
    }

    public getEntrenadorDestinatario(id: number): Destinatario {
        let aux = this.entrenadores.get(id);
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
}