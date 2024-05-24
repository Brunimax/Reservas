import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pessoa from './pessoa';
import Hotel from './hotel';
import Quarto from './quarto';
import TipoQuarto from './tipo-quarto';
import FotoQuarto from './foto-quarto';
import Reserva from './reserva';
import Avaliacao from './avaliacao';
import Pais from './pais';
import Estado from './estado';
import Municipio from './municipio';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="pessoa/*" element={<Pessoa />} />
        <Route path="hotel/*" element={<Hotel />} />
        <Route path="quarto/*" element={<Quarto />} />
        <Route path="tipo-quarto/*" element={<TipoQuarto />} />
        <Route path="foto-quarto/*" element={<FotoQuarto />} />
        <Route path="reserva/*" element={<Reserva />} />
        <Route path="avaliacao/*" element={<Avaliacao />} />
        <Route path="pais/*" element={<Pais />} />
        <Route path="estado/*" element={<Estado />} />
        <Route path="municipio/*" element={<Municipio />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
