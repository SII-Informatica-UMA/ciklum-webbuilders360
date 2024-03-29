import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { firstValueFrom, Observable, map } from 'rxjs';
import { Mensaje } from "./mensaje";
@Injectable({
    providedIn: 'root'
})
export class DestinatarioService {
    private baseURL: string = 'http://localhost:8080/mensaje/centro';

    constructor(private http: HttpClient, private destinatarioService: DestinatarioService) {}

    public async getMensajes(): Promise<Mensaje[]> {
        let mensajesDTO: MensajeDTO[] = await firstValueFrom(this.http.get<MensajeDTO[]>(this.baseURL));
        return this.procesarMensajesDTO(mensajesDTO);
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
        // Procesar
        return [];
    }
}
