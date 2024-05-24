import { IEstado } from 'app/shared/model/estado.model';

export interface IMunicipio {
  id?: number;
  nome?: string | null;
  estado?: IEstado | null;
}

export const defaultValue: Readonly<IMunicipio> = {};
