import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMunicipio } from 'app/shared/model/municipio.model';
import { getEntitiesList, reset } from './municipio.reducer';
import PageCounter from 'app/shared/layout/pagination';

export const Municipio = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [input, setInput] = useState('');

  const municipioList = useAppSelector(state => state.municipio.entities);
  const loading = useAppSelector(state => state.municipio.loading);
  const totalItens = useAppSelector(state => state.municipio.totalItems);

  useEffect(() => {
    dispatch(getEntitiesList({ query: input, page: page }));
  }, [input, page]);

  useEffect(() => {
    dispatch(reset());
  }, []);

  return (
    <div>
      <h2 id="municipio-heading" data-cy="MunicipioHeading">
        <Translate contentKey="reservasApp.municipio.home.title">Municipios</Translate>
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
              placeholder="Buscar município por nome"
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
              navigate('/municipio/new');
            }}
          >
            <FontAwesomeIcon icon="plus" />
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {municipioList && municipioList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="reservasApp.municipio.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.municipio.sigla">Estado</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {municipioList.map((municipio, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{municipio.nome}</td>
                    <td>{municipio.estado.sigla}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          className="btn-visualizar"
                          tag={Link}
                          to={`/municipio/${municipio.id}`}
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
                          to={`/municipio/${municipio.id}/edit`}
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
                          onClick={() => (location.href = `/municipio/${municipio.id}/delete`)}
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
              <Translate contentKey="reservasApp.municipio.home.notFound">No Municipios found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Municipio;
