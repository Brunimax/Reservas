import { IQuarto } from 'app/shared/model/quarto.model';

export interface IFotoQuarto {
  id?: number;
  fotoContentType?: string | null;
  foto?: string | null;
  status?: boolean | null;
  quarto?: IQuarto | null;
}

export const defaultValue: Readonly<IFotoQuarto> = {
  status: false,
};
