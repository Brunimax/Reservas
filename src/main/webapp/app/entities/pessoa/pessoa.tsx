import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesList, reset } from './pessoa.reducer';
import PageCounter from 'app/shared/layout/pagination';

export const Pessoa = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [input, setInput] = useState('');

  const pessoaList = useAppSelector(state => state.pessoa.entities);
  const loading = useAppSelector(state => state.pessoa.loading);
  const totalItens = useAppSelector(state => state.pessoa.totalItems);

  useEffect(() => {
    dispatch(getEntitiesList({ query: input, page: page }));
  }, [input, page]);

  useEffect(() => {
    dispatch(reset());
  }, []);

  return (
    <div>
      <h2 id="pessoa-heading" data-cy="PessoaHeading">
        <Translate contentKey="reservasApp.pessoa.home.title">Pessoas</Translate>
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
              placeholder="Buscar pessoa por Nome ou CPF"
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
              navigate('/pessoa/new');
            }}
          >
            <FontAwesomeIcon icon="plus" />
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {pessoaList && pessoaList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.nome">Nome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.sobrenome">Sobrenome</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.cpf">CPF</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.dataNascimento">Data Nascimento</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.telefone">Telefone</Translate>
                  </th>
                  <th>
                    <Translate contentKey="reservasApp.pessoa.email">Email</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {pessoaList.map((pessoa, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{pessoa.nome}</td>
                    <td>{pessoa.sobrenome}</td>
                    <td>{pessoa.cpf}</td>
                    <td>{pessoa.dataNascimento}</td>
                    <td>{pessoa.telefone}</td>
                    <td>{pessoa.email}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          className="btn-visualizar"
                          tag={Link}
                          to={`/pessoa/${pessoa.id}`}
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
                          to={`/pessoa/${pessoa.id}/edit`}
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
                          onClick={() => (location.href = `/pessoa/${pessoa.id}/delete`)}
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
              <Translate contentKey="reservasApp.pessoa.home.notFound">No Pessoas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Pessoa;
