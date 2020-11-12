package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Resource;
import cr.ac.ucenfotec.fun4fund.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Resource}.
 */
@Service
@Transactional
public class ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    /**
     * Save a resource.
     *
     * @param resource the entity to save.
     * @return the persisted entity.
     */
    public Resource save(Resource resource) {
        log.debug("Request to save Resource : {}", resource);
        return resourceRepository.save(resource);
    }

    /**
     * Get all the resources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Resource> findAll() {
        log.debug("Request to get all Resources");
        return resourceRepository.findAll();
    }


    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Resource> findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        resourceRepository.deleteById(id);
    }
}
