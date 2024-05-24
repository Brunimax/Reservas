import pessoa from 'app/entities/pessoa/pessoa.reducer';
import hotel from 'app/entities/hotel/hotel.reducer';
import quarto from 'app/entities/quarto/quarto.reducer';
import tipoQuarto from 'app/entities/tipo-quarto/tipo-quarto.reducer';
import fotoQuarto from 'app/entities/foto-quarto/foto-quarto.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import avaliacao from 'app/entities/avaliacao/avaliacao.reducer';
import pais from 'app/entities/pais/pais.reducer';
import estado from 'app/entities/estado/estado.reducer';
import municipio from 'app/entities/municipio/municipio.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  pessoa,
  hotel,
  quarto,
  tipoQuarto,
  fotoQuarto,
  reserva,
  avaliacao,
  pais,
  estado,
  municipio,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
