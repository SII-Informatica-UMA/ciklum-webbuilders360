import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class DestinatarioService {
    private clienteURL: string = 'http://localhost:8080/cliente'; // Cambiar
    private entrenadorURL: string = 'http://localhost:8080/entrenador'; // Cambiar
    private gerenteURL: string = 'http://localhost:8080/cliente'; // Cambiar
    private usuarioURL: string = 'http://localhost:8080/cliente'; // Cambiar
    private clientes: Destinatario[] = [];
    private entrenadores: Destinatario[] = [];
    private gerentes: Destinatario[] = [];

    constructor(private http: HttpClient) {
        // inicializar variables
        this.inicializarArrays();
    }

    private async inicializarArrays() {
        // Inicializar
        let [clientesDTO, entrenadoresDTO, gerentesDTO] = await Promise.all([this.http.get<ClienteDTO[]>(this.clienteURL),
                                                                            this.http.get<EntrenadorDTO[]>(this.entrenadorURL),
                                                                            this.http.get<GerenteDTO[]>(this.gerenteURL)]);
        
    }

    private procesarClientesDTO(clientesDTO: ClienteDTO[], usuariosDTO: UsuarioDTO[]): void {
        
    }

    public getCliente(id: number): Destinatario {
        return {id: -1, nombre: ""};
    }

    public getEntrenador(id: number): Destinatario {
        return {id: -1, nombre: ""};
    }

    public getGerente(id: number): Destinatario {
        return {id: -1, nombre: ""};
    }

}