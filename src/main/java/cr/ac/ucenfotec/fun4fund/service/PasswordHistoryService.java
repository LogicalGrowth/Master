package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.PasswordHistory;
import cr.ac.ucenfotec.fun4fund.repository.PasswordHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PasswordHistory}.
 */
@Service
@Transactional
public class PasswordHistoryService {

    private final Logger log = LoggerFactory.getLogger(PasswordHistoryService.class);

    private final PasswordHistoryRepository passwordHistoryRepository;

    public PasswordHistoryService(PasswordHistoryRepository passwordHistoryRepository) {
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    /**
     * Save a passwordHistory.
     *
     * @param passwordHistory the entity to save.
     * @return the persisted entity.
     */
    public PasswordHistory save(PasswordHistory passwordHistory) {
        log.debug("Request to save PasswordHistory : {}", passwordHistory);
        return passwordHistoryRepository.save(passwordHistory);
    }

    /**
     * Get all the passwordHistories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PasswordHistory> findAll() {
        log.debug("Request to get all PasswordHistories");
        return passwordHistoryRepository.findAll();
    }


    /**
     * Get one passwordHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PasswordHistory> findOne(Long id) {
        log.debug("Request to get PasswordHistory : {}", id);
        return passwordHistoryRepository.findById(id);
    }

    /**
     * Delete the passwordHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PasswordHistory : {}", id);
        passwordHistoryRepository.deleteById(id);
    }
}
