package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.AppLog;
import cr.ac.ucenfotec.fun4fund.repository.AppLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AppLog}.
 */
@Service
@Transactional
public class AppLogService {

    private final Logger log = LoggerFactory.getLogger(AppLogService.class);

    private final AppLogRepository appLogRepository;

    public AppLogService(AppLogRepository appLogRepository) {
        this.appLogRepository = appLogRepository;
    }

    /**
     * Save a appLog.
     *
     * @param appLog the entity to save.
     * @return the persisted entity.
     */
    public AppLog save(AppLog appLog) {
        log.debug("Request to save AppLog : {}", appLog);
        return appLogRepository.save(appLog);
    }

    /**
     * Get all the appLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppLog> findAll() {
        log.debug("Request to get all AppLogs");
        return appLogRepository.findAll();
    }


    /**
     * Get one appLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppLog> findOne(Long id) {
        log.debug("Request to get AppLog : {}", id);
        return appLogRepository.findById(id);
    }

    /**
     * Delete the appLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppLog : {}", id);
        appLogRepository.deleteById(id);
    }
}
