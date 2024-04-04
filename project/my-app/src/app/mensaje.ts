
export interface Mensaje {
  asunto: string;

  destinatarios: 
  {
    id: number;
    tipo: string;
  }[];

  copia: 
  {
    id: number;
    tipo: string;
  }[];

  copiaOculta: 
  {
    id: number;
    tipo: string;
  }[];

  contenido: string;
  
  idMensaje: number;
}

