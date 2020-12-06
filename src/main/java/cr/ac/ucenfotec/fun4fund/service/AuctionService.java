package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Auction;
import cr.ac.ucenfotec.fun4fund.domain.AuctionAnswerStatistics;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import cr.ac.ucenfotec.fun4fund.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private final ApplicationUserRepository applicationUserRepository;

    private final UserService userService;

    public AuctionService(
        AuctionRepository auctionRepository,
        ApplicationUserRepository applicationUserRepository,
        UserService userService
    ) {
        this.auctionRepository = auctionRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.userService = userService;
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

    public List<AuctionAnswerStatistics> getWinnersAuctionByMonth(int numberMonths) {
        List<Integer> months = new ArrayList<Integer>();
        List<Integer> years = new ArrayList<Integer>();
        LocalDate actual = LocalDate.now();
        months.add(actual.getMonthValue());
        years.add(actual.getYear());
        for (int i = 0; i < numberMonths - 1; i++) {
            actual = actual.minusMonths(1);
            months.add(actual.getMonthValue());
            years.add(actual.getYear());
        }
        Optional<ApplicationUser> winner = applicationUserRepository.findByInternalUserId(userService.getUserWithAuthorities().get().getId());

        return auctionRepository.getWinnerAuctionsByMonth(winner.get(), months, years);
    }
}
