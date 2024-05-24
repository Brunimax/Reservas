import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pessoa from './pessoa';
import PessoaDetail from './pessoa-detail';
import PessoaUpdate from './pessoa-update';
import PessoaDeleteDialog from './pessoa-delete-dialog';

const PessoaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pessoa />} />
    <Route path="new" element={<PessoaUpdate />} />
    <Route path=":id">
      <Route index element={<PessoaDetail />} />
      <Route path="edit" element={<PessoaUpdate />} />
      <Route path="delete" element={<PessoaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PessoaRoutes;
