import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estado from './estado';
import EstadoDetail from './estado-detail';
import EstadoUpdate from './estado-update';
import EstadoDeleteDialog from './estado-delete-dialog';

const EstadoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Estado />} />
    <Route path="new" element={<EstadoUpdate />} />
    <Route path=":id">
      <Route index element={<EstadoDetail />} />
      <Route path="edit" element={<EstadoUpdate />} />
      <Route path="delete" element={<EstadoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EstadoRoutes;
