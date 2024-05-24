import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './avaliacao.reducer';

export const AvaliacaoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const avaliacaoEntity = useAppSelector(state => state.avaliacao.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="avaliacaoDetailsHeading">
          <Translate contentKey="reservasApp.avaliacao.detail.title">Avaliacao</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{avaliacaoEntity.id}</dd>
          <dt>
            <span id="pontos">
              <Translate contentKey="reservasApp.avaliacao.pontos">Pontos</Translate>
            </span>
          </dt>
          <dd>{avaliacaoEntity.pontos}</dd>
          <dt>
            <span id="comentario">
              <Translate contentKey="reservasApp.avaliacao.comentario">Comentario</Translate>
            </span>
          </dt>
          <dd>{avaliacaoEntity.comentario}</dd>
        </dl>
        <Button tag={Link} to="/avaliacao" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/avaliacao/${avaliacaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AvaliacaoDetail;
