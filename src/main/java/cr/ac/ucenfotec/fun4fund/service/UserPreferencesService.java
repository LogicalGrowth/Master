package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.UserPreferences;
import cr.ac.ucenfotec.fun4fund.repository.UserPreferencesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserPreferences}.
 */
@Service
@Transactional
public class UserPreferencesService {

    private final Logger log = LoggerFactory.getLogger(UserPreferencesService.class);

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    /**
     * Save a userPreferences.
     *
     * @param userPreferences the entity to save.
     * @return the persisted entity.
     */
    public UserPreferences save(UserPreferences userPreferences) {
        log.debug("Request to save UserPreferences : {}", userPreferences);
        return userPreferencesRepository.save(userPreferences);
    }

    /**
     * Get all the userPreferences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserPreferences> findAll() {
        log.debug("Request to get all UserPreferences");
        return userPreferencesRepository.findAll();
    }


    /**
     * Get one userPreferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserPreferences> findOne(Long id) {
        log.debug("Request to get UserPreferences : {}", id);
        return userPreferencesRepository.findById(id);
    }

    /**
     * Delete the userPreferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserPreferences : {}", id);
        userPreferencesRepository.deleteById(id);
    }
}
