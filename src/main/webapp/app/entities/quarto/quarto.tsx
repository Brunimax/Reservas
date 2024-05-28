import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesList, reset } from './quarto.reducer';
import PageCounter from 'app/shared/layout/pagination';

export const Quarto = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [input, setInput] = useState('');

  const quartoList = useAppSelector(state => state.quarto.entities);
  const loading = useAppSelector(state => state.quarto.loading);
  const totalItens = useAppSelector(state => state.quarto.totalItems);

  useEffect(() => {
    dispatch(getEntitiesList({ query: input, page: page }));
  }, [input, page]);

  useEffect(() => {
    dispatch(reset());
  }, []);

  return (
    <div>
      <h2 id="quarto-heading" data-cy="QuartoHeading">
        <Translate contentKey="reservasApp.quarto.home.title">Quartos</Translate>
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
              placeholder="Buscar quarto por nome"
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
              navigate('/quarto/new');
            }}
          >
            <FontAwesomeIcon icon="plus" />
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {quartoList && quartoList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="reservasApp.quarto.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.quarto.classificacao">Classificação</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.quarto.hotel">Hotel</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.quarto.tipoQuarto">Tipo Quarto</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {quartoList.map((quarto, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{quarto.nome}</td>
                    <td>{quarto.classificacao}</td>
                    <td>{quarto.hotel.nome}</td>
                    <td>{quarto.tipoQuarto.nome}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          className="btn-visualizar"
                          tag={Link}
                          to={`/quarto/${quarto.id}`}
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
                          to={`/quarto/${quarto.id}/edit`}
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
                          onClick={() => (location.href = `/quarto/${quarto.id}/delete`)}
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
              <Translate contentKey="reservasApp.quarto.home.notFound">No Quartos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Quarto;
