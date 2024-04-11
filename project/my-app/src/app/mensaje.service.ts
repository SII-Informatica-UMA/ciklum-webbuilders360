import { HttpClient } from "@angular/common/http";
import { firstValueFrom } from 'rxjs';
import { Mensaje } from "./mensaje";
import { Destinatario } from "./destinatario";
import { DestinatarioService } from "./destinatario.service";
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
        let centroId: number | undefined = this.usuariosService._rolCentro?.centro;
        if (centroId !== undefined) {
            let mensajesDTO: MensajeDTO[] =
                await firstValueFrom(this.http.get<MensajeDTO[]>(this.baseURLPorCentro + centroId.toString()));
            return this.procesarMensajesDTO(mensajesDTO);
        } else {
            //TODO Excepción
            return [];
        }
    }

    private async procesarMensajesDTO(mensajesDTO: MensajeDTO[]): Promise<Mensaje[]> {
        let mensajes: Mensaje[] = [];
        for (let mensajeDTO of mensajesDTO) {
            mensajes.push(await this.mensajeDTO2Mensaje(mensajeDTO));
        }
        return mensajes;
    }

    private async mensajeDTO2Mensaje(mensajeDTO: MensajeDTO): Promise<Mensaje> {
        let nombre: string | undefined = this.usuariosService.rolCentro?.nombreCentro;
        if (nombre !== undefined) {
            let remitente: DestinatarioDTO = await this.destinatarioService.nombre2DestinatarioDTO(nombre);
            return new Mensaje(mensajeDTO.asunto,
                await this.procesarDestinatariosDTO(mensajeDTO.destinatarios),
                await this.procesarDestinatariosDTO(mensajeDTO.copia),
                await this.procesarDestinatariosDTO(mensajeDTO.copiaOculta),
                await this.destinatarioService.destinatarioDTO2Destinatario(remitente),
                mensajeDTO.contenido,
                mensajeDTO.idMensaje);
        } else {
            //TODO Excepción
            return new Mensaje("", [], [], [], new Destinatario(-1, "", ""), "", -1);
        }
    }

    private async procesarDestinatariosDTO(destinatariosDTO: DestinatarioDTO[]): Promise<Destinatario[]> {
        let destinatarios: Destinatario[] = [];
        for (let destinatarioDTO of destinatariosDTO) {
            destinatarios.push(await this.destinatarioService.destinatarioDTO2Destinatario(destinatarioDTO));
        }
        return destinatarios;
    }

    public eliminarMensaje(id: number) {
        this.http.delete(this.baseURLPorId + id);
    }

    public async enviarMensaje(asunto: string,
                               destinatarios: string[],
                               copia: string[],
                               copiaOculta: string[],
                               remitente: string,
                               contenido: string): Promise<Mensaje> {

            let mensajePOST: MensajePOST = {asunto: asunto,
                                            destinatarios: await this.destinatarioService.nombres2DestinatariosDTO(destinatarios),
                                            copia: await this.destinatarioService.nombres2DestinatariosDTO(copia),
                                            copiaOculta: await this.destinatarioService.nombres2DestinatariosDTO(copiaOculta),
                                            remitente: await this.destinatarioService.nombre2DestinatarioDTO(remitente),
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
    

    private async destinatarios2DestinatariosDTO(destinatarios: Destinatario[]): Promise<DestinatarioDTO[]> {
        let destinatariosDTO: DestinatarioDTO[] = [];
        for (let destinatario of destinatarios) {
            destinatariosDTO.push(await this.destinatarioService.destinatario2DestinatarioDTO(destinatario));
        }
        return destinatariosDTO;
    }
    */

    public async getNombresDestinatarios(): Promise<readonly string[]> {
        return await this.destinatarioService.getNombresDestinatarios();
    }
}
