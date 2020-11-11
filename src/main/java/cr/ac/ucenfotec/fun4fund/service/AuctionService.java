package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Auction}.
 */
@Service
@Transactional
public class AuctionService {

    private final Logger log = LoggerFactory.getLogger(AuctionService.class);

    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    /**
     * Save a auction.
     *
     * @param auction the entity to save.
     * @return the persisted entity.
     */
    public Auction save(Auction auction) {
        log.debug("Request to save Auction : {}", auction);
        return auctionRepository.save(auction);
    }

    /**
     * Get all the auctions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Auction> findAll() {
        log.debug("Request to get all Auctions");
        return auctionRepository.findAll();
    }


    /**
     * Get one auction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Auction> findOne(Long id) {
        log.debug("Request to get Auction : {}", id);
        return auctionRepository.findById(id);
    }

    /**
     * Delete the auction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Auction : {}", id);
        auctionRepository.deleteById(id);
    }
}
