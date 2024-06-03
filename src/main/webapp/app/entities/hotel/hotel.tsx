import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesList, reset } from './hotel.reducer';
import PageCounter from 'app/shared/layout/pagination';

export const Hotel = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [input, setInput] = useState('');

  const hotelList = useAppSelector(state => state.hotel.entities);
  const loading = useAppSelector(state => state.hotel.loading);
  const totalItens = useAppSelector(state => state.hotel.totalItems);

  useEffect(() => {
    dispatch(getEntitiesList({ query: input, page: page }));
  }, [input, page]);

  useEffect(() => {
    dispatch(reset());
  }, []);

  return (
    <div>
      <h2 id="hotel-heading" data-cy="HotelHeading">
        <Translate contentKey="reservasApp.hotel.home.title">Hoteis</Translate>
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
              placeholder="Buscar hotel por nome"
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
              navigate('/hotel/new');
            }}
          >
            <FontAwesomeIcon icon="plus" />
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {hotelList && hotelList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="reservasApp.hotel.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.hotel.classificacao">Classificação</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.hotel.quantidadeQuartos">Quantidade de Quartos</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.hotel.vagas">Vagas</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.hotel.municipio">Municipio</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.hotel.bairro">Bairro</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {hotelList.map((hotel, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{hotel.nome}</td>
                    <td>{hotel.classificacao}</td>
                    <td>{hotel.quantidadeQuartos}</td>
                    <td>{hotel.vagas}</td>
                    <td>{hotel.bairro}</td>
                    <td>{hotel.municipio.nome}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          className="btn-visualizar"
                          tag={Link}
                          to={`/hotel/${hotel.id}`}
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
                          to={`/hotel/${hotel.id}/edit`}
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
                          onClick={() => (location.href = `/hotel/${hotel.id}/delete`)}
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
              <Translate contentKey="reservasApp.hotel.home.notFound">No Hoteis found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Hotel;
