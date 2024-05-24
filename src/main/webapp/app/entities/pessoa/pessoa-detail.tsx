import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pessoa.reducer';

export const PessoaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pessoaEntity = useAppSelector(state => state.pessoa.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pessoaDetailsHeading">
          <Translate contentKey="reservasApp.pessoa.detail.title">Pessoa</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.pessoa.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.nome}</dd>
          <dt>
            <span id="sobrenome">
              <Translate contentKey="reservasApp.pessoa.sobrenome">Sobrenome</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.sobrenome}</dd>
          <dt>
            <span id="cpf">
              <Translate contentKey="reservasApp.pessoa.cpf">Cpf</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.cpf}</dd>
          <dt>
            <span id="dataNascimento">
              <Translate contentKey="reservasApp.pessoa.dataNascimento">Data Nascimento</Translate>
            </span>
          </dt>
          <dd>
            {pessoaEntity.dataNascimento ? <TextFormat value={pessoaEntity.dataNascimento} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="telefone">
              <Translate contentKey="reservasApp.pessoa.telefone">Telefone</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.telefone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="reservasApp.pessoa.email">Email</Translate>
            </span>
          </dt>
          <dd>{pessoaEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/pessoa" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pessoa/${pessoaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PessoaDetail;
