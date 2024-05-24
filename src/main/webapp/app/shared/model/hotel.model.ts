import { IMunicipio } from 'app/shared/model/municipio.model';
import { IPessoa } from 'app/shared/model/pessoa.model';

export interface IHotel {
  id?: number;
  nome?: string | null;
  quantidadeQuartos?: number | null;
  vagas?: number | null;
  classificacao?: number | null;
  status?: boolean | null;
  cep?: string | null;
  bairro?: string | null;
  endereco?: string | null;
  municipio?: IMunicipio | null;
  pessoa?: IPessoa | null;
}

export const defaultValue: Readonly<IHotel> = {
  status: false,
};
