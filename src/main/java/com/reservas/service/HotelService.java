package com.reservas.service;

import com.reservas.domain.Hotel;
import com.reservas.repository.HotelRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.reservas.domain.Hotel}.
 */
@Service
@Transactional
public class HotelService {

    private final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Save a hotel.
     *
     * @param hotel the entity to save.
     * @return the persisted entity.
     */
    public Hotel save(Hotel hotel) {
        log.debug("Request to save Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Update a hotel.
     *
     * @param hotel the entity to save.
     * @return the persisted entity.
     */
    public Hotel update(Hotel hotel) {
        log.debug("Request to update Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Partially update a hotel.
     *
     * @param hotel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hotel> partialUpdate(Hotel hotel) {
        log.debug("Request to partially update Hotel : {}", hotel);

        return hotelRepository
            .findById(hotel.getId())
            .map(existingHotel -> {
                if (hotel.getNome() != null) {
                    existingHotel.setNome(hotel.getNome());
                }
                if (hotel.getQuantidadeQuartos() != null) {
                    existingHotel.setQuantidadeQuartos(hotel.getQuantidadeQuartos());
                }
                if (hotel.getVagas() != null) {
                    existingHotel.setVagas(hotel.getVagas());
                }
                if (hotel.getClassificacao() != null) {
                    existingHotel.setClassificacao(hotel.getClassificacao());
                }
                if (hotel.getStatus() != null) {
                    existingHotel.setStatus(hotel.getStatus());
                }
                if (hotel.getCep() != null) {
                    existingHotel.setCep(hotel.getCep());
                }
                if (hotel.getBairro() != null) {
                    existingHotel.setBairro(hotel.getBairro());
                }
                if (hotel.getEndereco() != null) {
                    existingHotel.setEndereco(hotel.getEndereco());
                }

                return existingHotel;
            })
            .map(hotelRepository::save);
    }

    /**
     * Get all the hotels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        log.debug("Request to get all Hotels");
        return hotelRepository.findAll();
    }

    /**
     * Get one hotel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hotel> findOne(Long id) {
        log.debug("Request to get Hotel : {}", id);
        return hotelRepository.findById(id);
    }

    /**
     * Delete the hotel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hotel : {}", id);
        hotelRepository.deleteById(id);
    }
}
