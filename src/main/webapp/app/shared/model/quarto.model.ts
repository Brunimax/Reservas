import { IHotel } from 'app/shared/model/hotel.model';
import { ITipoQuarto } from 'app/shared/model/tipo-quarto.model';

export interface IQuarto {
  id?: number;
  nome?: string | null;
  quantidadeHospedes?: number | null;
  status?: boolean | null;
  classificacao?: number | null;
  hotel?: IHotel | null;
  tipoQuarto?: ITipoQuarto | null;
}

export const defaultValue: Readonly<IQuarto> = {
  status: false,
};
