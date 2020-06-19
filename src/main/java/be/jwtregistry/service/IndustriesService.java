package be.jwtregistry.service;

import be.jwtregistry.domain.Industries;
import be.jwtregistry.repository.IndustriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Industries}.
 */
@Service
@Transactional
public class IndustriesService {

    private final Logger log = LoggerFactory.getLogger(IndustriesService.class);

    private final IndustriesRepository industriesRepository;

    public IndustriesService(IndustriesRepository industriesRepository) {
        this.industriesRepository = industriesRepository;
    }

    /**
     * Save a industries.
     *
     * @param industries the entity to save.
     * @return the persisted entity.
     */
    public Industries save(Industries industries) {
        log.debug("Request to save Industries : {}", industries);
        return industriesRepository.save(industries);
    }

    /**
     * Get all the industries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Industries> findAll() {
        log.debug("Request to get all Industries");
        return industriesRepository.findAll();
    }


    /**
     * Get one industries by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Industries> findOne(Long id) {
        log.debug("Request to get Industries : {}", id);
        return industriesRepository.findById(id);
    }

    /**
     * Delete the industries by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Industries : {}", id);
        industriesRepository.deleteById(id);
    }
}
