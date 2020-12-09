package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserService.class);

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     * Save a applicationUser.
     *
     * @param applicationUser the entity to save.
     * @return the persisted entity.
     */
    public ApplicationUser save(ApplicationUser applicationUser) {
        log.debug("Request to save ApplicationUser : {}", applicationUser);
        return applicationUserRepository.save(applicationUser);
    }

    /**
     * Get all the applicationUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUser> findAll() {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll();
    }


    /**
     * Get one applicationUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationUser> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findById(id);
    }

    /**
     * Delete the applicationUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
    }
}
