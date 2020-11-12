package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.repository.RaffleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Raffle}.
 */
@Service
@Transactional
public class RaffleService {

    private final Logger log = LoggerFactory.getLogger(RaffleService.class);

    private final RaffleRepository raffleRepository;

    public RaffleService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    /**
     * Save a raffle.
     *
     * @param raffle the entity to save.
     * @return the persisted entity.
     */
    public Raffle save(Raffle raffle) {
        log.debug("Request to save Raffle : {}", raffle);
        return raffleRepository.save(raffle);
    }

    /**
     * Get all the raffles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Raffle> findAll() {
        log.debug("Request to get all Raffles");
        return raffleRepository.findAll();
    }


    /**
     * Get one raffle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Raffle> findOne(Long id) {
        log.debug("Request to get Raffle : {}", id);
        return raffleRepository.findById(id);
    }

    /**
     * Delete the raffle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Raffle : {}", id);
        raffleRepository.deleteById(id);
    }
}
