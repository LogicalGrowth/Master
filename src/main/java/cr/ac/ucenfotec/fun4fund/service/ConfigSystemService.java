package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ConfigSystem;
import cr.ac.ucenfotec.fun4fund.repository.ConfigSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ConfigSystem}.
 */
@Service
@Transactional
public class ConfigSystemService {

    private final Logger log = LoggerFactory.getLogger(ConfigSystemService.class);

    private final ConfigSystemRepository configSystemRepository;

    public ConfigSystemService(ConfigSystemRepository configSystemRepository) {
        this.configSystemRepository = configSystemRepository;
    }

    /**
     * Save a configSystem.
     *
     * @param configSystem the entity to save.
     * @return the persisted entity.
     */
    public ConfigSystem save(ConfigSystem configSystem) {
        log.debug("Request to save ConfigSystem : {}", configSystem);
        return configSystemRepository.save(configSystem);
    }

    /**
     * Get all the configSystems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigSystem> findAll() {
        log.debug("Request to get all ConfigSystems");
        return configSystemRepository.findAll();
    }


    /**
     * Get one configSystem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigSystem> findOne(Long id) {
        log.debug("Request to get ConfigSystem : {}", id);
        return configSystemRepository.findById(id);
    }

    public List<ConfigSystem> findByType(String type) {
        log.debug("Request to get all ConfigSystems");
        return configSystemRepository.findByType(type);
    }

    /**
     * Delete the configSystem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConfigSystem : {}", id);
        configSystemRepository.deleteById(id);
    }
}
