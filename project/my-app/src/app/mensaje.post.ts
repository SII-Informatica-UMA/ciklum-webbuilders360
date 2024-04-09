import { DestinatarioDTO } from "./destinatario.dto";

export interface MensajePOST {
    asunto: string,
    destinatarios: DestinatarioDTO[],
    copia: DestinatarioDTO[],
    copiaOculta: DestinatarioDTO[],
    remitente: DestinatarioDTO,
    contenido: string,
}