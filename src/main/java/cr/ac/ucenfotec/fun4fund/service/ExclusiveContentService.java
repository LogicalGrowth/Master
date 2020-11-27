package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ExclusiveContent;
import cr.ac.ucenfotec.fun4fund.repository.ExclusiveContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ExclusiveContent}.
 */
@Service
@Transactional
public class ExclusiveContentService {

    private final Logger log = LoggerFactory.getLogger(ExclusiveContentService.class);

    private final ExclusiveContentRepository exclusiveContentRepository;

    public ExclusiveContentService(ExclusiveContentRepository exclusiveContentRepository) {
        this.exclusiveContentRepository = exclusiveContentRepository;
    }

    /**
     * Save a exclusiveContent.
     *
     * @param exclusiveContent the entity to save.
     * @return the persisted entity.
     */
    public ExclusiveContent save(ExclusiveContent exclusiveContent) {
        log.debug("Request to save ExclusiveContent : {}", exclusiveContent);
        return exclusiveContentRepository.save(exclusiveContent);
    }

    /**
     * Get all the exclusiveContents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExclusiveContent> findAll() {
        log.debug("Request to get all ExclusiveContents");
        return exclusiveContentRepository.findAll();
    }


    /**
     * Get one exclusiveContent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExclusiveContent> findOne(Long id) {
        log.debug("Request to get ExclusiveContent : {}", id);
        return exclusiveContentRepository.findById(id);
    }

    /**
     * Delete the exclusiveContent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExclusiveContent : {}", id);
        exclusiveContentRepository.deleteById(id);
    }
}
