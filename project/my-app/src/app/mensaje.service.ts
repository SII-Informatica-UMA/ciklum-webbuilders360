import { HttpClient, HttpParams } from "@angular/common/http";
import { firstValueFrom, Observable, map } from 'rxjs';
import { Mensaje } from "./mensaje";
import { Destinatario } from "./destinatario";
import { DestinatarioService } from "./destinatario.service";

export class MesajeService {
    private baseURL: string = 'http://localhost:8080/mensaje/centro?centro=';
    private destinatarioService: DestinatarioService;

    constructor(private http: HttpClient, private centrosID: number[]) {
        this.destinatarioService = new DestinatarioService(http, centrosID);
    }

    public async getMensajes(): Promise<Mensaje[]> {
        let mensajes: Mensaje[] = [];
        for (let centroID of this.centrosID) {
            let mensajesDTO: MensajeDTO[] = await firstValueFrom(this.http.get<MensajeDTO[]>(this.baseURL));
            mensajes = mensajes.concat(this.procesarMensajesDTO(mensajesDTO));
        }
        return mensajes;
    }

    private procesarMensajesDTO(mensajesDTO: MensajeDTO[]): Mensaje[] {
        let mensajes: Mensaje[] = [];
        for (let mensajeDTO of mensajesDTO) {
            mensajes.push(new Mensaje(mensajeDTO.asunto,
                                      this.procesarDestinatariosDTO(mensajeDTO.destinatarios),
                                      this.procesarDestinatariosDTO(mensajeDTO.copia),
                                      this.procesarDestinatariosDTO(mensajeDTO.copiaOculta),
                                      mensajeDTO.contenido,
                                      mensajeDTO.idMensaje));
        }
        return mensajes;
    }

    private procesarDestinatariosDTO(destinatariosDTO: DestinatarioDTO[]): Destinatario[] {
        let destinatarios: Destinatario[] = [];
        for (let destinatarioDTO of destinatariosDTO) {
            switch(destinatarioDTO.tipo) {
                case "CLIENTE":
                    destinatarios.push(this.destinatarioService.getClienteDestinatario(destinatarioDTO.id));
                    break;
                case "ENTRENADOR":
                    destinatarios.push(this.destinatarioService.getEntrenadorDestinatario(destinatarioDTO.id));
                    break;
            }
        }
        return destinatarios;
    }
}
