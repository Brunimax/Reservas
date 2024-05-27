import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesList, reset } from './estado.reducer';
import PageCounter from 'app/shared/layout/pagination';

export const Estado = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [input, setInput] = useState('');

  const estadoList = useAppSelector(state => state.estado.entities);
  const loading = useAppSelector(state => state.estado.loading);
  const totalItens = useAppSelector(state => state.estado.totalItems);

  useEffect(() => {
    dispatch(getEntitiesList({ query: input, page: page }));
  }, [input, page]);

  useEffect(() => {
    dispatch(reset());
  }, []);

  return (
    <div>
      <h2 id="estado-heading" data-cy="EstadoHeading">
        <Translate contentKey="reservasApp.estado.home.title">Estados</Translate>
        <div className="d-flex justify-content-end">
          <div className="input-group-header">
            <input
              onChange={e => {
                setInput(e.target.value);
                setPage(0);
              }}
              value={input || ''}
              className="input-pesquisa"
              required
              type="text"
              id="pesquisa"
              style={{ borderTopRightRadius: '5px', borderTopLeftRadius: '5px' }}
              name="pesquisa"
              placeholder="Buscar estado por nome"
            />
          </div>
          <Button className="button-header pesquisa" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="magnifying-glass" />
          </Button>
          <Button
            className="button-header pesquisa"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => {
              navigate('/estado/new');
            }}
          >
            <FontAwesomeIcon icon="plus" />
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {estadoList && estadoList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="reservasApp.estado.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.estado.sigla">Sigla</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {estadoList.map((estado, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{estado.nome}</td>
                    <td>{estado.sigla}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          className="btn-visualizar"
                          tag={Link}
                          to={`/estado/${estado.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          className="btn-editar"
                          tag={Link}
                          to={`/estado/${estado.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (location.href = `/estado/${estado.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            <PageCounter totalItens={totalItens} page={page} setPage={setPage} />
          </>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="reservasApp.estado.home.notFound">No Estados found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Estado;
