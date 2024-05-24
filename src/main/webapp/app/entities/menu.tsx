import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/pessoa">
        <Translate contentKey="global.menu.entities.pessoa" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/hotel">
        <Translate contentKey="global.menu.entities.hotel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/quarto">
        <Translate contentKey="global.menu.entities.quarto" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tipo-quarto">
        <Translate contentKey="global.menu.entities.tipoQuarto" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/foto-quarto">
        <Translate contentKey="global.menu.entities.fotoQuarto" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reserva">
        <Translate contentKey="global.menu.entities.reserva" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/avaliacao">
        <Translate contentKey="global.menu.entities.avaliacao" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pais">
        <Translate contentKey="global.menu.entities.pais" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/estado">
        <Translate contentKey="global.menu.entities.estado" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/municipio">
        <Translate contentKey="global.menu.entities.municipio" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
