import { DestinatarioDTO } from "./destinatario.dto"

export interface MensajeDTO {
    asunto: string,
    destinatarios: DestinatarioDTO[],
    copia: DestinatarioDTO[],
    copiaOculta: DestinatarioDTO[],
    contenido: string,
    idMensaje: number
}