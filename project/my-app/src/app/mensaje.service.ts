import { HttpClient, HttpParams } from "@angular/common/http";
import { firstValueFrom, Observable, map } from 'rxjs';
import { Mensaje } from "./mensaje";
import { Destinatario } from "./destinatario";
import { DestinatarioService } from "./destinatario.service";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { MensajeDTO } from "./mensaje.dto";
import { DestinatarioDTO } from "./destinatario.dto";
import { MensajePOST } from "./mensaje.post";
import { Injectable } from "@angular/core";
import { UsuariosService } from "./services/usuarios.service";

@Injectable({
    providedIn: 'root'
})
export class MensajeService {
    private baseURL: string = 'http://localhost:8080/mensaje/centro/';
    private baseURLPorId: string = 'http://localhost:8080/mensaje/centro/';
    private baseURLPorCentro: string = 'http://localhost:8080/mensaje/centro?centro=';

    constructor(private http: HttpClient,
                private usuariosService: UsuariosService,
                private destinatarioService: DestinatarioService) {}

    public async getMensajes(): Promise<Mensaje[]> {
        let centroId: string | undefined = this.usuariosService._rolCentro?.centro?.toString();
        let mensajesDTO: MensajeDTO[] = await firstValueFrom(this.http.get<MensajeDTO[]>(this.baseURLPorCentro + centroId));
        return this.procesarMensajesDTO(mensajesDTO);
    }

    private procesarMensajesDTO(mensajesDTO: MensajeDTO[]): Mensaje[] {
        let mensajes: Mensaje[] = [];
        for (let mensajeDTO of mensajesDTO) {
            mensajes.push(this.mensajeDTO2Mensaje(mensajeDTO));
        }
        return mensajes;
    }

    private mensajeDTO2Mensaje(mensajeDTO: MensajeDTO): Mensaje {
        return new Mensaje(mensajeDTO.asunto,
            this.procesarDestinatariosDTO(mensajeDTO.destinatarios),
            this.procesarDestinatariosDTO(mensajeDTO.copia),
            this.procesarDestinatariosDTO(mensajeDTO.copiaOculta),
            mensajeDTO.contenido,
            mensajeDTO.idMensaje);
    }

    private procesarDestinatariosDTO(destinatariosDTO: DestinatarioDTO[]): Destinatario[] {
        let destinatarios: Destinatario[] = [];
        for (let destinatarioDTO of destinatariosDTO) {
            destinatarios.push(this.destinatarioService.destinatarioDTO2Destinatario(destinatarioDTO));
        }
        return destinatarios;
    }

    public eliminarMensaje(id: number) {
        this.http.delete(this.baseURLPorId + id);
    }

    public async enviarMensaje(asunto: string, destinatarios: string[], copia: string[], copiaOculta: string[],
        contenido: string): Promise<Mensaje> {
            let mensajePOST: MensajePOST = {asunto: asunto,
                                            destinatarios: this.destinatarioService.nombres2DestinatariosDTO(destinatarios),
                                            copia: this.destinatarioService.nombres2DestinatariosDTO(copia),
                                            copiaOculta: this.destinatarioService.nombres2DestinatariosDTO(copiaOculta),
                                            contenido: contenido};
            let mensajeDTO: MensajeDTO = await firstValueFrom(this.http.post<MensajeDTO>(this.baseURL, mensajePOST));
            return this.mensajeDTO2Mensaje(mensajeDTO);
    }

    /*
    private mensaje2MensajePOST(mensaje: Mensaje): MensajePOST {
        return {asunto: mensaje.getAsunto(),
                destinatarios: this.destinatarios2DestinatariosDTO(mensaje.getDestinatarios()),
                copia: this.destinatarios2DestinatariosDTO(mensaje.getCopia()),
                copiaOculta: this.destinatarios2DestinatariosDTO(mensaje.getCopiaOculta()),
                contenido: mensaje.getContenido()}
    }
    */

    public destinatarios2DestinatariosDTO(destinatarios: Destinatario[]): DestinatarioDTO[] {
        let destinatariosDTO: DestinatarioDTO[] = [];
        for (let destinatario of destinatarios) {
            destinatariosDTO.push(this.destinatarioService.destinatario2DestinatarioDTO(destinatario));
        }
        return destinatariosDTO;
    }

    public getNombresDestinatarios(): readonly string[] {
        return this.destinatarioService.getNombresDestinatarios();
    }
}
