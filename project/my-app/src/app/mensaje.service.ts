import { HttpClient, HttpParams } from "@angular/common/http";
import { firstValueFrom, Observable, map } from 'rxjs';
import { Mensaje } from "./mensaje";
import { Destinatario } from "./destinatario";
import { DestinatarioService } from "./destinatario.service";

export class MensajeService {
    private baseURL: string = 'http://localhost:8080/mensaje/centro';
    private destinatarioService: DestinatarioService;

    constructor(private http: HttpClient, private centrosID: number[]) {
        this.destinatarioService = new DestinatarioService(http, centrosID);
    }

    public async getMensajes(): Promise<Mensaje[]> {
        let mensajes: Mensaje[] = [];
        for (let centroID of this.centrosID) {
            let mensajesDTO: MensajeDTO[] =
                await firstValueFrom(this.http.get<MensajeDTO[]>(this.baseURL + "?centro=" + centroID.toString()));
            mensajes = mensajes.concat(this.procesarMensajesDTO(mensajesDTO));
        }
        return mensajes;
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
        this.http.delete(this.baseURL + '/' + id);
    }

    public async enviarMensaje(mensajePOST: MensajePOST): Promise<Mensaje> {
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
