import { IPais } from 'app/shared/model/pais.model';

export interface IEstado {
  id?: number;
  nome?: string | null;
  sigla?: string | null;
  pais?: IPais | null;
}

export const defaultValue: Readonly<IEstado> = {};
