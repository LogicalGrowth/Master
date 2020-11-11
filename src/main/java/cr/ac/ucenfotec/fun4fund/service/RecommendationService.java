package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Recommendation;
import cr.ac.ucenfotec.fun4fund.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Recommendation}.
 */
@Service
@Transactional
public class RecommendationService {

    private final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Save a recommendation.
     *
     * @param recommendation the entity to save.
     * @return the persisted entity.
     */
    public Recommendation save(Recommendation recommendation) {
        log.debug("Request to save Recommendation : {}", recommendation);
        return recommendationRepository.save(recommendation);
    }

    /**
     * Get all the recommendations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Recommendation> findAll() {
        log.debug("Request to get all Recommendations");
        return recommendationRepository.findAll();
    }


    /**
     * Get one recommendation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Recommendation> findOne(Long id) {
        log.debug("Request to get Recommendation : {}", id);
        return recommendationRepository.findById(id);
    }

    /**
     * Delete the recommendation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Recommendation : {}", id);
        recommendationRepository.deleteById(id);
    }
}
