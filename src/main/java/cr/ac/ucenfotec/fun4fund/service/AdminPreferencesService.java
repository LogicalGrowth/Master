package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.AdminPreferences;
import cr.ac.ucenfotec.fun4fund.repository.AdminPreferencesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AdminPreferences}.
 */
@Service
@Transactional
public class AdminPreferencesService {

    private final Logger log = LoggerFactory.getLogger(AdminPreferencesService.class);

    private final AdminPreferencesRepository adminPreferencesRepository;

    public AdminPreferencesService(AdminPreferencesRepository adminPreferencesRepository) {
        this.adminPreferencesRepository = adminPreferencesRepository;
    }

    /**
     * Save a adminPreferences.
     *
     * @param adminPreferences the entity to save.
     * @return the persisted entity.
     */
    public AdminPreferences save(AdminPreferences adminPreferences) {
        log.debug("Request to save AdminPreferences : {}", adminPreferences);
        return adminPreferencesRepository.save(adminPreferences);
    }

    /**
     * Get all the adminPreferences.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AdminPreferences> findAll() {
        log.debug("Request to get all AdminPreferences");
        return adminPreferencesRepository.findAll();
    }


    /**
     * Get one adminPreferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdminPreferences> findOne(Long id) {
        log.debug("Request to get AdminPreferences : {}", id);
        return adminPreferencesRepository.findById(id);
    }

    /**
     * Delete the adminPreferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdminPreferences : {}", id);
        adminPreferencesRepository.deleteById(id);
    }
}
