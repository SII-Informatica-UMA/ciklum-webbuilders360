export class Destinatario {
    public constructor(private id: number, private nombre: string, private tipo: string) {}

    public getID(): number {
        return this.id;
    }

    public getNombre(): string {
        return this.nombre;
    }

    public getTipo(): string {
        return this.tipo;
    }
}