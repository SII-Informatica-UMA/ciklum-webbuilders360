import { Destinatario } from "./destinatario";

export class Mensaje {
    
    public constructor(public asunto: string,
                       private destinatarios: Destinatario[],
                       private copia: Destinatario[],
                       private copiaOculta: Destinatario[],
                       private remitente: Destinatario,
                       private contenido: string,
                       private idMensaje: number) {
                        console.log(remitente);
                       }
    
    public getAsunto(): string {
        return this.asunto;
    }

    public getDestinatarios(): Destinatario[] {
        return this.destinatarios;
    }

    public getCopia(): Destinatario[] {
        return this.copia;
    }

    public getCopiaOculta(): Destinatario[] {
        return this.copiaOculta;
    }

    public getRemitente(): Destinatario {
        return this.remitente;
    }

    public getContenido(): string {
        return this.contenido;
    }

    public getIdMensaje(): number {
        return this.idMensaje;
    }
}