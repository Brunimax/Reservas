import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuarto } from 'app/shared/model/quarto.model';
import { getEntities as getQuartos } from 'app/entities/quarto/quarto.reducer';
import { IFotoQuarto } from 'app/shared/model/foto-quarto.model';
import { getEntity, updateEntity, createEntity, reset } from './foto-quarto.reducer';

export const FotoQuartoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const quartos = useAppSelector(state => state.quarto.entities);
  const fotoQuartoEntity = useAppSelector(state => state.fotoQuarto.entity);
  const loading = useAppSelector(state => state.fotoQuarto.loading);
  const updating = useAppSelector(state => state.fotoQuarto.updating);
  const updateSuccess = useAppSelector(state => state.fotoQuarto.updateSuccess);

  const handleClose = () => {
    navigate('/foto-quarto');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuartos({}));
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
      ...fotoQuartoEntity,
      ...values,
      quarto: quartos.find(it => it.id.toString() === values.quarto?.toString()),
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
          ...fotoQuartoEntity,
          quarto: fotoQuartoEntity?.quarto?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reservasApp.fotoQuarto.home.createOrEditLabel" data-cy="FotoQuartoCreateUpdateHeading">
            <Translate contentKey="reservasApp.fotoQuarto.home.createOrEditLabel">Create or edit a FotoQuarto</Translate>
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
                  id="foto-quarto-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('reservasApp.fotoQuarto.foto')}
                id="foto-quarto-foto"
                name="foto"
                data-cy="foto"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('reservasApp.fotoQuarto.status')}
                id="foto-quarto-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <ValidatedField
                id="foto-quarto-quarto"
                name="quarto"
                data-cy="quarto"
                label={translate('reservasApp.fotoQuarto.quarto')}
                type="select"
              >
                <option value="" key="0" />
                {quartos
                  ? quartos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/foto-quarto" replace color="info">
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

export default FotoQuartoUpdate;
