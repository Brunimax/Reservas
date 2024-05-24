export interface IAvaliacao {
  id?: number;
  pontos?: number | null;
  comentario?: string | null;
}

export const defaultValue: Readonly<IAvaliacao> = {};
