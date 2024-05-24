import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './foto-quarto.reducer';

export const FotoQuartoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fotoQuartoEntity = useAppSelector(state => state.fotoQuarto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fotoQuartoDetailsHeading">
          <Translate contentKey="reservasApp.fotoQuarto.detail.title">FotoQuarto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fotoQuartoEntity.id}</dd>
          <dt>
            <span id="foto">
              <Translate contentKey="reservasApp.fotoQuarto.foto">Foto</Translate>
            </span>
          </dt>
          <dd>
            {fotoQuartoEntity.foto ? (
              <div>
                {fotoQuartoEntity.fotoContentType ? (
                  <a onClick={openFile(fotoQuartoEntity.fotoContentType, fotoQuartoEntity.foto)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {fotoQuartoEntity.fotoContentType}, {byteSize(fotoQuartoEntity.foto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="reservasApp.fotoQuarto.status">Status</Translate>
            </span>
          </dt>
          <dd>{fotoQuartoEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="reservasApp.fotoQuarto.quarto">Quarto</Translate>
          </dt>
          <dd>{fotoQuartoEntity.quarto ? fotoQuartoEntity.quarto.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/foto-quarto" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/foto-quarto/${fotoQuartoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FotoQuartoDetail;
