import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPais } from 'app/shared/model/pais.model';
import { getEntities as getPais } from 'app/entities/pais/pais.reducer';
import { IEstado } from 'app/shared/model/estado.model';
import { getEntity, updateEntity, createEntity, reset } from './estado.reducer';

export const EstadoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pais = useAppSelector(state => state.pais.entities);
  const estadoEntity = useAppSelector(state => state.estado.entity);
  const loading = useAppSelector(state => state.estado.loading);
  const updating = useAppSelector(state => state.estado.updating);
  const updateSuccess = useAppSelector(state => state.estado.updateSuccess);

  const handleClose = () => {
    navigate('/estado');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPais({}));
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

    const entity = {
      ...estadoEntity,
      ...values,
      pais: pais.find(it => it.id.toString() === values.pais?.toString()),
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
          ...estadoEntity,
          pais: estadoEntity?.pais?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.estado.home.createOrEditLabel" data-cy="EstadoCreateUpdateHeading">
            <Translate contentKey="reservasApp.estado.home.createOrEditLabel">Create or edit a Estado</Translate>
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
                  id="estado-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('reservasApp.estado.nome')} id="estado-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField label={translate('reservasApp.estado.sigla')} id="estado-sigla" name="sigla" data-cy="sigla" type="text" />
              <ValidatedField id="estado-pais" name="pais" data-cy="pais" label={translate('reservasApp.estado.pais')} type="select">
                <option value="" key="0" />
                {pais
                  ? pais.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/estado" replace color="info">
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

export default EstadoUpdate;
