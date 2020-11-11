package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Prize;
import cr.ac.ucenfotec.fun4fund.repository.PrizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Prize}.
 */
@Service
@Transactional
public class PrizeService {

    private final Logger log = LoggerFactory.getLogger(PrizeService.class);

    private final PrizeRepository prizeRepository;

    public PrizeService(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    /**
     * Save a prize.
     *
     * @param prize the entity to save.
     * @return the persisted entity.
     */
    public Prize save(Prize prize) {
        log.debug("Request to save Prize : {}", prize);
        return prizeRepository.save(prize);
    }

    /**
     * Get all the prizes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Prize> findAll() {
        log.debug("Request to get all Prizes");
        return prizeRepository.findAll();
    }


    /**
     * Get one prize by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Prize> findOne(Long id) {
        log.debug("Request to get Prize : {}", id);
        return prizeRepository.findById(id);
    }

    /**
     * Delete the prize by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prize : {}", id);
        prizeRepository.deleteById(id);
    }
}
