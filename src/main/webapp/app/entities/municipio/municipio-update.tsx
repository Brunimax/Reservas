import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstado } from 'app/shared/model/estado.model';
import { getEntities as getEstados } from 'app/entities/estado/estado.reducer';
import { IMunicipio } from 'app/shared/model/municipio.model';
import { getEntity, updateEntity, createEntity, reset } from './municipio.reducer';

export const MunicipioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estados = useAppSelector(state => state.estado.entities);
  const municipioEntity = useAppSelector(state => state.municipio.entity);
  const loading = useAppSelector(state => state.municipio.loading);
  const updating = useAppSelector(state => state.municipio.updating);
  const updateSuccess = useAppSelector(state => state.municipio.updateSuccess);

  const handleClose = () => {
    navigate('/municipio');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstados({}));
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
      ...municipioEntity,
      ...values,
      estado: estados.find(it => it.id.toString() === values.estado?.toString()),
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
          ...municipioEntity,
          estado: municipioEntity?.estado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.municipio.home.createOrEditLabel" data-cy="MunicipioCreateUpdateHeading">
            <Translate contentKey="reservasApp.municipio.home.createOrEditLabel">Create or edit a Municipio</Translate>
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
                  id="municipio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('reservasApp.municipio.nome')} id="municipio-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField
                id="municipio-estado"
                name="estado"
                data-cy="estado"
                label={translate('reservasApp.municipio.estado')}
                type="select"
              >
                <option value="" key="0" />
                {estados
                  ? estados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/municipio" replace color="info">
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

export default MunicipioUpdate;
