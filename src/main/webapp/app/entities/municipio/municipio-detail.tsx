import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './municipio.reducer';

export const MunicipioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const municipioEntity = useAppSelector(state => state.municipio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="municipioDetailsHeading">
          <Translate contentKey="reservasApp.municipio.detail.title">Municipio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{municipioEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="reservasApp.municipio.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{municipioEntity.nome}</dd>
          <dt>
            <Translate contentKey="reservasApp.municipio.estado">Estado</Translate>
          </dt>
          <dd>{municipioEntity.estado ? municipioEntity.estado.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/municipio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/municipio/${municipioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MunicipioDetail;
