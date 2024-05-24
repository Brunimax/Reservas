import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAvaliacao } from 'app/shared/model/avaliacao.model';
import { getEntity, updateEntity, createEntity, reset } from './avaliacao.reducer';

export const AvaliacaoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const avaliacaoEntity = useAppSelector(state => state.avaliacao.entity);
  const loading = useAppSelector(state => state.avaliacao.loading);
  const updating = useAppSelector(state => state.avaliacao.updating);
  const updateSuccess = useAppSelector(state => state.avaliacao.updateSuccess);

  const handleClose = () => {
    navigate('/avaliacao');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.pontos !== undefined && typeof values.pontos !== 'number') {
      values.pontos = Number(values.pontos);
    }

    const entity = {
      ...avaliacaoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...avaliacaoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.avaliacao.home.createOrEditLabel" data-cy="AvaliacaoCreateUpdateHeading">
            <Translate contentKey="reservasApp.avaliacao.home.createOrEditLabel">Create or edit a Avaliacao</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="avaliacao-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('reservasApp.avaliacao.pontos')}
                id="avaliacao-pontos"
                name="pontos"
                data-cy="pontos"
                type="text"
              />
              <ValidatedField
                label={translate('reservasApp.avaliacao.comentario')}
                id="avaliacao-comentario"
                name="comentario"
                data-cy="comentario"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/avaliacao" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AvaliacaoUpdate;
