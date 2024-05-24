import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Avaliacao from './avaliacao';
import AvaliacaoDetail from './avaliacao-detail';
import AvaliacaoUpdate from './avaliacao-update';
import AvaliacaoDeleteDialog from './avaliacao-delete-dialog';

const AvaliacaoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Avaliacao />} />
    <Route path="new" element={<AvaliacaoUpdate />} />
    <Route path=":id">
      <Route index element={<AvaliacaoDetail />} />
      <Route path="edit" element={<AvaliacaoUpdate />} />
      <Route path="delete" element={<AvaliacaoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AvaliacaoRoutes;
