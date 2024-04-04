import { Destinatario } from "./destinatario";

export class Mensaje {
    
    public constructor(private asunto: string,
                       private destinatarios: Destinatario[],
                       private copia: Destinatario[],
                       private copiaOculta: Destinatario[],
                       private contenido: string,
                       private idMensaje: number) {}
    
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

    public getContenido(): string {
        return this.contenido;
    }

    public getIdMensaje(): number {
        return this.idMensaje;
    }
}
